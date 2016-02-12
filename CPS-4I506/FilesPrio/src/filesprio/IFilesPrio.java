package filesprio;

import java.util.Queue;
import java.util.Set;

public interface IFilesPrio<T> {
	/**********************************
	 *          OBSERVATEURS  
	 **********************************/
	int size();
	boolean empty();
	Set<Integer> activePrios();
	boolean isActivePrio(int n);
	int maxPrio();
	int sizePrio(int n);
	// \pre getPrio(i) require sizePrio(i)>0
	T getPrio(int prio);
	// \pre get() require size()>0
	T get();
	// \pre getElem(i,k) require i ∈ activePrios() ^ 0 < k <= sizePrio(i)
	T getElem(int pri, int n);
	
	/**********************************
	 *          CONSTRUCTEURS  
	 **********************************/
	
	// \post size() == 0
	public void init();
	
	/**********************************
	 *          OPERATEURS  
	 **********************************/
	
	// \pre putPrio(i, e) require i >= 0 && e != null
	// \post isActivePrio(i) && (activePrios()@pre == activePrios()) 
	// \post not(isActivePrio(i)) && (activePrios() == activePrios()@pre +1)
	// \post sizePrio(i) = sizePrio(i)@pre + 1
	void putPrio(int prio, T elem);
	// \pre put(e) require e != null
	void put(T elem);
	// \pre removePrio(i) require sizePrio(i) > 0
	void removePrio(int prio);
	// \pre remove() require sizePrio(i) > 0 
	void remove(T elem);
	
	/**********************************
	 *          INVARIANTS  
	 **********************************/
	/**
	 * \inv size() = \sum sizePrio(i) \forall i \in activePrios()
	 * \inv empty() = (size() == 0)
	 * \inv isActivePrio(i) = i ∈ activePrios()
	 * \inv maxPrio() = max{activePrios()
	 * \inv getPrio(i) = getElement(i, 1)
	 * \inv get() = getPrio(maxPrio())
	 * \inv \forall i \in activePrios(), sizePrio(i) > 0
	 * \inv \forall i \notin activePrios(), sizePrio(i) == 0
	 * \inv \forall i \in activePrios(), \forall k \in [1..sizePrio(i)], getElem(i, k) != null
	 */
	
}
