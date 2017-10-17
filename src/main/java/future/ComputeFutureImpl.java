package future;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.*;
public class ComputeFutureImpl implements ComputeFuture {
	boolean done;
	boolean cancelled;
	boolean success;
	Throwable cause;
	List<ComputeFutureListener> listeners = new CopyOnWriteArrayList<ComputeFutureListener>();
	Object result;
	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return done;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return cancelled;
	}

	@Override
	public boolean isSuccess() {
		// TODO Auto-generated method stub
		return success;
	}

	@Override
	public Throwable getCause() {
		// TODO Auto-generated method stub
		return cause;
	}

	@Override
	public boolean cancel() {
		cancelled = true;
		return true;
	}

	@Override
	public boolean setSuccess(Object result) {
		success = true;
		this.result = result;
		done = true;
		return true;
	}

	@Override
	public boolean setFailure(Throwable cause) {
		success = false;
		this.cause = cause;
		done = true;
		return true;
	}

	@Override
	public void addListener(ComputeFutureListener listener) {
		if(listener == null){
			throw new NullPointerException();
		}
		synchronized (listener) {
			if(!listeners.contains(listener)){
				listeners.add(listener);
			}
		}
			
	}

	@Override
	public void removeListener(ComputeFutureListener listener) {
		listeners.remove(listener);
	}

	@Override
	public ComputeFuture sync() throws InterruptedException {
		return null;
	}

	@Override
	public ComputeFuture syncUninterruptibly() {
		return null;
	}

	@Override
	public ComputeFuture await() throws InterruptedException {
		return null;
	}

	@Override
	public ComputeFuture awaitUninterruptibly() {
		return null;
	}

	@Override
	public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
		return false;
	}

	@Override
	public boolean await(long timeoutMillis) throws InterruptedException {
		return false;
	}

	@Override
	public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
		return false;
	}

	@Override
	public boolean awaitUninterruptibly(long timeoutMillis) {
		return false;
	}

	@Override
	public Object getResult() {
		return null;
	}

}
