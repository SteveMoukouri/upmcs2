package filesprio;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class FilesPrioImplem<T> implements IFilesPrio<T> {
	
	protected SortedMap<Integer, List<T>> file;
	
	public FilesPrioImplem() {
		super();
		init();
	}
	
	@Override
	public int size() {
		int sum = 0;
		for (Integer i : file.keySet())
			sum += file.get(i).size();
		return sum;
	}

	@Override
	public boolean empty() {
		return file.isEmpty();
	}

	@Override
	public Set<Integer> activePrios() {
		return new HashSet<Integer>(file.keySet());
	}

	@Override
	public boolean isActivePrio(int n) {
		return file.keySet().contains(n);
	}

	@Override
	public int maxPrio() {
		return Collections.max(file.keySet());
	}

	@Override
	public int sizePrio(int n) {
		if (file.get(n) == null)
            return 0;
		return file.get(n).size();
	}

	@Override
	public T getPrio(int prio) {
		return file.get(prio).get(0);
	}

	@Override
	public T get() {
		return getPrio(maxPrio());
	}

	@Override
	public T getElem(int pri, int n) {
		return file.get(pri).get(n);
	}

	@Override
	public void init() {
		file=new TreeMap<Integer,List<T>>();
	}

	@Override
	public void putPrio(int prio, T elem) {
		if (! file.containsKey(prio)) {
			file.put(prio, new LinkedList<T>());
		}
		file.get(prio).add(elem);
	}

	@Override
	public void put(T elem) {
		putPrio(maxPrio(), elem);
	}

	@Override
	public void removePrio(int prio) {
		List<T> l = file.get(prio);
		l.remove(0);
		if (l.isEmpty())
			file.remove(prio);
	}

	@Override
	public void remove(T elem) {
		removePrio(maxPrio());
	}

}
