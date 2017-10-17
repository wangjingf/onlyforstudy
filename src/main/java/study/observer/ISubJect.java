package study.observer;

public interface ISubJect {
	public void addObserver(IObserver observer);
	public void removeObserver(IObserver observer);
	public void destory();
	public void start();
}
