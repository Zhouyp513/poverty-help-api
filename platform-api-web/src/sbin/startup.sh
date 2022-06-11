#!/usr/bin/env bash


#JDK路径
#JAVA_HOME=""

#利用pwd命令拿到当前工程目录，实际拿到到的是该shell脚本的目录。再利用sed命令将/bin替换为空
Project_HOME=$(echo `pwd` | sed 's/\/sbin//')

LOG_DIR=$Project_HOME/log
APPLICATION_MAIN=cn.poverty.WebPlatformApplication
CLASSPATH=$Project_HOME/classes


PORT=9002
DEBUG_PORT=8002 #调试端口

#EVN=development
#EVN=default
#EVN=production
#EVN=dev
EVN=$1


#JVM启动参数
#-server:一定要作为第一个参数,在多个CPU时性能佳
#-Xloggc:记录GC日志,这里建议写成绝对路径,如此便可在任意目录下执行该shell脚本
#JAVA_OPTS="-ms512m -mx512m -Xmn256m -Djava.awt.headless=true -XX:MaxPermSize=128m"
#JAVA_OPTS="-Dspring.profiles.active=$EVN -Dserver.port=$PORT -Duser.timezone=GMT+8 -server -Xms256m -Xmx512m -Xloggc:${LOG_DIR}/gc.log"
#-XX:PermSize=256M JVM初始分配的非堆内存 -XX:MaxPermSize=2048M JVM最大允许分配的非堆内存，按需分配
#上面的配置在Java8里面使用-XX:MetaspaceSize=200m -XX:MaxMetaspaceSize=256m代替
#-XX:MaxNewSize=1024m 最大的新生代的内存
#禁止代码中显式调用GC (System.gc()无效)
#使用并发标记清除（CMS）收集器UseConcMarkSweepGC
#降低标记停顿CMSParallelRemarkEnabled
#原始类型的快速优化 UseFastAccessorMethods
#-XX:LargePageSizeInBytes  指定 Java heap 的分页页面大小
#UseCMSInitiatingOccupancyOnly  使用手动定义的初始化定义开始CMS收集
#CMSInitiatingOccupancyFraction 使用cms作为垃圾回收使用70％后开始CMS收集
#-Xss规定了每个线程堆栈的大小。一般情况下256K是足够了。影响了此进程中并发线程数大小。
#-XX:+AggressiveHeap 使用大量的物理内存,长时间大内存使用的优化，能检查计算资源（内存， 处理器数量）,至少需要256MB内存,大量的CPU／内存
#-XX:CMSInitiatingOccupancyFraction=70代表CMS在对内存占用率达到70%的时候开始GC(因为CMS会有浮动垃圾,所以一般都较早启动GC)
#-XX:+UseCMSInitiatingOccupancyOnly 设定的回收阈值(上面指定的70%),如果不指定,JVM仅在第一次使用设定值,后续则自动调整
#-XX:+CMSScavengeBeforeRemark在CMS GC前启动一次ygc，目的在于减少old gen对ygc gen的引用，降低remark时的开销-----因为CMS的GC耗时 80%都在remark阶段
#-XX:+UseAdaptiveSizePolicy 自动选择年轻代区大小和相应的Survivor区比例
#-XX:+UseParallelOldGC 年老代垃圾收集方式为并行收集(Parallel Compacting)
#-XX:ParallelGCThreads 并行收集器的线程数此值最好配置与处理器数目相等 同样适用于CMS
#-XX:+UseParallelGC 择垃圾收集器为并行收集器.此配置仅对年轻代有效.即上述配置下,年轻代使用并发收集,而年老代仍旧使用串行收集
#-XX:+UseParNewGC 设置年轻代为并行收集 JDK5以上会自动设置,所以无需配置
#-XX:+UseFastAccessorMethods 原始类型的快速优化 -XX:+AggressiveOpts 快速编译 -XX:+UseBiasedLocking 锁机制的性能改善
#-XX:+DisableExplicitGC 禁止代码里面显式调用GC
#-XX:MaxMetaspaceSize=256M 设置元空间为256M
#-XX:+PrintGCTimeStamps 输出GC的时间戳（以基准时间的形式）
#-XX:+PrintGCDateStamps 输出GC的时间戳（以日期的形式，如 2019-1-2T21:53:59.234+0800）
#-XX:+UseCMSCompactAtFullCollection 使用并发收集器时,开启对年老代的压缩UseCMSCompactAtFullCollection
#-verbose:gc 在输出设备上显示虚拟机运行信息

JAVA_OPTS="-Dspring.profiles.active=$EVN
  -Dserver.port=$PORT -Duser.timezone=GMT+8 -server
  -Djava.awt.headless=true
  -Xdebug -Xrunjdwp:transport=dt_socket,suspend=n,server=y,address=${DEBUG_PORT}
  -Xmx128m -Xms128m -Xmn128m -XX:PermSize=128m -Xss256k -XX:+DisableExplicitGC
  -XX:MaxMetaspaceSize=512M -verbose:gc
  -XX:MetaspaceSize=200m -XX:MaxMetaspaceSize=256m
  -XX:+UseG1GC -XX:MaxGCPauseMillis=200
  -XX:InitiatingHeapOccupancyPercent=45
  -XX:NewRatio=2 -XX:SurvivorRatio=8
  -XX:MaxTenuringThreshold=15
  -XX:ParallelGCThreads=20
  -XX:ConcGCThreads=20
  -XX:G1HeapRegionSize=32m
  -XX:+UseFastAccessorMethods
  -XX:LargePageSizeInBytes=128m
  -XX:+PrintGC
  -XX:+UseGCLogFileRotation
  -XX:NumberOfGCLogFiles=10
  -XX:GCLogFileSize=20M
  -XX:+PrintGCDetails
  -XX:+PrintGCDateStamps
  -XX:+PrintGCTimeStamps
  -XX:+PrintHeapAtGC
  -XX:+TraceClassUnloading
  -XX:+PrintCompilation
  -XX:+CITime
  -Xloggc:${LOG_DIR}/gc-%t.log
  -XX:ErrorFile=${LOG_DIR}/java_error_%p.log
  -XX:HeapDumpPath=${LOG_DIR}/java_error.hprof"

for jarfile in "$Project_HOME"/lib/*.jar
do
   CLASSPATH="$CLASSPATH":"$jarfile"
done



#-------------------------------------------------------------------------------------------------------------
#getPID()-->拿到Java应用的PID
#说明:通过JDK自带的JPS命令及grep命令,精准查找Java应用的PID
#其中:[jps -l]表示显示Java主程序的完整包路径
#     awk命令可以分割出PID($1部分)及Java主程序名称($2部分)
#例子:[$jps -l | grep $APPLICATION_MAIN]-->>[5775 jrx.anytxn.cms.App]
#另外:用这个命令也可以直接取到程序的PID-->>[ps aux|grep java|grep $APPLICATION_MAIN|grep -v grep|awk '{print $2}']
#-------------------------------------------------------------------------------------------------------------
#初始化全局变量tradePortalPID,用于标识交易前置系统的PID,0表示未启动
TPID=0

getPID(){
    javaps=`jps -l | grep $APPLICATION_MAIN`
    if [ -n "$javaps" ]; then
        TPID=`echo $javaps | awk '{print $1}'`
    else
        TPID=0
    fi
}

logDir(){
    if [! -d "$LOG_DIR"]; then
        mkdir "$LOG_DIR"
    fi
}

#清理系统缓存
clearSystemCache(){

echo ">>>>>>>>>>>>>>>>>>>>>>开始执行清除" $EVN "系统环境环境缓存<<<<<<<<<<<<<<<<<<<<<<<<<<<"

#drop_caches的值可以是0-3之间的数字，代表不同的含义：
#0：不释放（系统默认值）
#1：释放页缓存
#2：释放dentries和inodes
#3：释放所有缓存

#释放内存前先使用sync命令做同步，以保证文件系统的完整性，将所有未写的系统缓冲区写到磁盘中
#包含已修改的 i-node、已延迟的块 I/O 和读写映射文件。否则在释放缓存的过程中，可能会丢失未保存的文件。
echo ">>>>>>>>>>>>>>>>>>>>>>执行清理文件系统缓存和无用对象和它们占用的内存<<<<<<<<<<<<<<<<<<<<<<<<<<<"
sync

#清理pagecache（页面缓存）
echo ">>>>>>>>>>>>>>>>>>>>>>执行清理pagecache(页面缓存)<<<<<<<<<<<<<<<<<<<<<<<<<<<"
echo 1 > /proc/sys/vm/drop_caches

#清理dentries（目录缓存）和inodes
echo ">>>>>>>>>>>>>>>>>>>>>>执行清理dentries（目录缓存）和inodes<<<<<<<<<<<<<<<<<<<<<<<<<<<"
echo 2 > /proc/sys/vm/drop_caches

#清理pagecache、dentries和inodes
echo ">>>>>>>>>>>>>>>>>>>>>>执行清理pagecache、dentries和inodes<<<<<<<<<<<<<<<<<<<<<<<<<<<"
echo 3 > /proc/sys/vm/drop_caches

echo ">>>>>>>>>>>>>>>>>>>>>>释放完内存后让系统重新自动分配内存<<<<<<<<<<<<<<<<<<<<<<<<<<<"
echo 0 >/proc/sys/vm/drop_caches

}

startup(){
    getPID
    logDir
    clearSystemCache
    echo "---------------------------------------------------------------------------------------------------------------------------"
    echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>JAVA_OPTS:" $JAVA_OPTS "<<<<<<<<<<<<<<<<<<<<<<"
    if [ $TPID -ne 0 ]; then
        echo "$APP_MAIN already started(PID=$TPID)"
        echo "---------------------------------------------------------------------------------------------------------------------------"
    else
        echo -n "Starting $APPLICATION_MAIN"
        nohup java $JAVA_OPTS -classpath $CLASSPATH $APPLICATION_MAIN >/dev/null 2>&1 &
        getPID
        if [ $TPID -ne 0 ]; then
            echo "(PID=$TPID)...[Success]"
            echo "---------------------------------------------------------------------------------------------------------------------------"
        else
            echo "[Failed]"
            echo "---------------------------------------------------------------------------------------------------------------------------"
        fi
    fi
}

startup

#tail -f $LOG_DIR/nohup.log
