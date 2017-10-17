package interceptor;

import java.util.List;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

public class SoapHeadInterceptor  extends AbstractPhaseInterceptor<SoapMessage> {

	public SoapHeadInterceptor(String phase) {
		super(Phase.WRITE);
	}

	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		//message.add
	}

}
