package cn.poverty.common.config.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

/**
 * @author
 * @packageName cn.poverty.common.config.logback
 * @Description: Logback日志配置
 * @date 2021-04-24
 */
public class LogbackColorConfiguration extends ForegroundCompositeConverterBase<ILoggingEvent> {

    /**
     * 配置颜色
     * @author
     * @date 2021/4/24
     * @param event 事件
     * @return String
     */
    @Override
    protected String getForegroundColorCode(ILoggingEvent event) {
        Level level = event.getLevel();
          switch(level.toInt()) {
             case Level.ERROR_INT:
                        return ANSIConstants.RED_FG;
             case Level.WARN_INT:
                     return ANSIConstants.YELLOW_FG;
             case Level.INFO_INT:
                     return ANSIConstants.GREEN_FG;
             case Level.DEBUG_INT:
                     return ANSIConstants.BLUE_FG;
             case Level.TRACE_INT:
                     return ANSIConstants.DEFAULT_FG;
             default:
             return ANSIConstants.DEFAULT_FG;
          }
    }
}
