package future.baddesign;

public interface IService {
	public void exec(Runnable task);
	public boolean isSucces();
	public boolean isFailure();
	public void addListener(IFutureListener listener);
}
