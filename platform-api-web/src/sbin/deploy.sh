#!/usr/bin/env bash

#deploy.sh
#GIT_URL=git@gitee.com:Doubs/poverty-help.git
#GIT_APP_NAME=poverty-help
#ARTIFACT_ID=poverty-help-api
#MAVEN_HOME=/opt/maven/apache-maven-3.8.3
#EVN=$1

GIT_URL=$1
GIT_APP_NAME=$2 #GIT代码目录
ARTIFACT_ID=$3 #最终打包生成的包的名称xxx.tar.gz
BRANCH_NAME=$4
EVN=$5
MAVEN_HOME=/opt/maven/apache-maven-3.8.3

clear(){
  cd /opt
  #如果本来存在则执行关闭项目
  if [ ! -d "/opt/${ARTIFACT_ID}/" ];then
  echo "项目未启动"
  else
  cd /opt/${ARTIFACT_ID}/
  sh sbin/shutdown.sh
  fi
  if [ $? -ne 0 ]; then
    echo "----------------------------停止旧项目->结束-failed--------------------------------------------"
  else
    echo "----------------------------停止旧项目->结束-succeed--------------------------------------------"
  fi
  echo "----------------------------删除旧项目--------------------------------------------"
  if [ ! -d "/opt/${GIT_APP_NAME}/" ];then
  echo "文件夹/opt/${GIT_APP_NAME}/不存在"
  else
  rm -rf /opt/${GIT_APP_NAME}/
  fi
  if [ ! -f "/opt/${ARTIFACT_ID}.tar.gz" ];then
  echo "文件/opt/${ARTIFACT_ID}.tar.gz不存在"
  else
  rm -rf /opt/${ARTIFACT_ID}.tar.gz
  fi
  if [ ! -d "/opt/${ARTIFACT_ID}/" ];then
  echo "文件夹/opt/${ARTIFACT_ID}/不存在"
  else
  rm -rf /opt/${ARTIFACT_ID}/
  fi
  if [ $? -ne 0 ]; then
    echo "----------------------------删除旧项目->结束-failed--------------------------------------------"
  else
    echo "----------------------------删除旧项目->结束-succeed--------------------------------------------"
  fi
}

clone(){
  cd /opt
  echo "----------------------------clone项目:git clone -b ${BRANCH_NAME} ${GIT_URL} /opt/${GIT_APP_NAME}/--------------------------------------------"
  pwd
  #如果不存在则创建目录，存在则直接拉
  if [ ! -d "/opt/${GIT_APP_NAME}/" ];then
  mkdir /opt/${GIT_APP_NAME}/ && git clone -b ${BRANCH_NAME} ${GIT_URL} "/opt/${GIT_APP_NAME}/"
  else
  git clone -b ${BRANCH_NAME} ${GIT_URL} "/opt/${GIT_APP_NAME}/"
  fi
  if [ $? -ne 0 ]; then
    echo "----------------------------clone项目->结束-failed--------------------------------------------"
  else
    echo "----------------------------clone项目->结束-succeed--------------------------------------------"
  fi
}

package(){
  cd /opt
  echo "----------------------------执行打包--------------------------------------------"
  if [ ! -d "/opt/${GIT_APP_NAME}/${ARTIFACT_ID}" ];then
  echo "GIT项目文件不存在"
  else
  #拷贝后端项目到工作目录
  mv /opt/${GIT_APP_NAME}/${ARTIFACT_ID} /opt/${ARTIFACT_ID} && cd /opt/${ARTIFACT_ID}
  #执行打包
  mvn clean package -Dmaven.test.skip=true --settings ${MAVEN_HOME}/conf/settings.xml
  #移动打包输出结果
  mv /opt/${ARTIFACT_ID}/${ARTIFACT_ID}.tar.gz /opt/${ARTIFACT_ID}.tar.gz
  if [ $? -ne 0 ]; then
    echo "----------------------------执行打包->结束-failed--------------------------------------------"
  else
    echo "----------------------------执行打包->结束-succeed--------------------------------------------"
  fi
  fi
}

# 执行发布方法
deploy(){
  clear
  clone
  package
  echo "----------------------------解压文件--------------------------------------------"
  cd /opt
  tar -zxvf /opt/${ARTIFACT_ID}.tar.gz -C /opt/
  if [ $? -ne 0 ]; then
    echo "----------------------------解压文件->结束-failed--------------------------------------------"
  else
    echo "----------------------------解压文件->结束-succeed--------------------------------------------"
  fi
  echo "----------------------------启动项目--------------------------------------------"
  cd /opt/${ARTIFACT_ID}
  sh sbin/startup.sh ${EVN}
  if [ $? -ne 0 ]; then
    echo "----------------------------启动项目->结束-failed--------------------------------------------"
  else
    echo "----------------------------启动项目->结束-succeed--------------------------------------------"
  fi
}

deploy
