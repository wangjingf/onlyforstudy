package future.baddesign;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceImpl implements IService{
	boolean success;
	boolean failure;
	private List<IFutureListener> listeners = Collections.synchronizedList(new ArrayList<IFutureListener>());
	@Override
	public void exec(Runnable task) {
		try{
			task.run();
		}catch(Exception e){
			
		}
	}

	@Override
	public boolean isSucces() {
		return false;
	}

	@Override
	public boolean isFailure() {
		return false;
	}
	@Override
	public void addListener(IFutureListener listener) {
		listeners.add(listener);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setFailure(boolean failure) {
		this.failure = failure;
	}
	
}
