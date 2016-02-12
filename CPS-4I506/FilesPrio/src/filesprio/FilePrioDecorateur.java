package filesprio;

import java.util.Set;

public abstract class FilePrioDecorateur<T> implements IFilesPrio<T> {

	IFilesPrio<T> delegates;
	
	public FilePrioDecorateur(IFilesPrio<T> delegate) {
		this.delegates = delegate;
	}
	
	@Override
	public int size() {
		return delegates.size();
	}

	@Override
	public boolean empty() {
		return delegates.empty();
	}

	@Override
	public Set<Integer> activePrios() {
		return delegates.activePrios();
	}

	@Override
	public boolean isActivePrio(int n) {
		return delegates.isActivePrio(n);
	}

	@Override
	public int maxPrio() {
		return delegates.maxPrio();
	}

	@Override
	public int sizePrio(int n) {
		return delegates.sizePrio(n);
	}

	@Override
	public T getPrio(int prio) {
		return delegates.getPrio(prio);
	}

	@Override
	public T get() {
		return delegates.get();
	}

	@Override
	public T getElem(int pri, int n) {
		return delegates.getElem(pri, n);
	}

	@Override
	public void init() {
		delegates.init();
	}

	@Override
	public void putPrio(int prio, T elem) {
		delegates.putPrio(prio, elem);
	}

	@Override
	public void put(T elem) {
		delegates.put(elem);
	}

	@Override
	public void removePrio(int prio) {
		delegates.removePrio(prio);
	}

	@Override
	public void remove(T elem) {
		delegates.remove(elem);
	}

}
