 package io.study.gateway.registry;
 
 import com.jd.vd.common.exception.BizException;
 import com.jd.vd.common.exception.StdException;

 public class RegistryException
   extends StdException
 {
   private static final long serialVersionUID = 1L;
   public static final String ERR_NO_NODE = "registry.err_no_node";
   public static final String ERR_NODE_EXISTS = "registry.err_node_exists";
   public static final String ERR_CONNECTION_LOSS = "registry.err_connection_loss";
   public static final String ERR_FAIL = "registry.err_fail";
   public static final String ERR_EPHEMERAL_NODE_NOT_ALLOW_CHILD = "registry.err_ephemeral_node_not_allow_child";
   
   public RegistryException(String errorCode, Throwable cause) {
     super(errorCode, cause);
   }
   
   public RegistryException(String errorCode) {
     super(errorCode);
   }
   
   public boolean isNoNodeException() {
     return "registry.err_no_node".equals(getErrorCode());
   }
   
   public boolean isNodeExistsException() {
     return "registry.err_node_exists".equals(getErrorCode());
   }
   
   public boolean isConnectionLossException() {
     return "registry.err_connection_loss".equals(getErrorCode());
   }
 }


/* Location:              D:\docs\soa\th_platform\defaultroot\WEB-INF\lib\thu-cluster.jar!\edu\thu\service\registry\RegistryException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */