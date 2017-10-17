package study.observer;

import java.util.ArrayList;
import java.util.List;

public class SubjectImpl implements ISubJect {
	public List<IObserver> list = new ArrayList<IObserver>();
	@Override
	public void addObserver(IObserver observer) {
		// TODO Auto-generated method stub
		list.add(observer);
	}

	@Override
	public void removeObserver(IObserver observer) {
		// TODO Auto-generated method stub
		list.remove(observer);
	}

	@Override
	public void destory() {
		// TODO Auto-generated method stub
		for(IObserver observer:list){
			observer.destory();
		}
	}

	@Override
	public void start() {//xiu gaishi
		// TODO Auto-generated method stub
		for(IObserver observer:list){
			observer.start();
		}
	}

}
