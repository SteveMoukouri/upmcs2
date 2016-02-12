package filesprio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class FilesPrioContrat<T> extends FilePrioDecorateur<T> {

	public FilesPrioContrat(IFilesPrio<T> delegate) {
		super(delegate);
	}
	
	private void checkInvariants(){
		// \inv size() = \sum sizePrio(i) \forall i \in activePrios()
		int sum = 0;
		for(Integer i : delegates.activePrios()) 
			sum += sizePrio(i);
		if (!(sum == size()))
			throw new InvariantError("Problème de taille");
		// \inv empty() = (size() == 0)
		if (!(empty() == (size() == 0)))
			throw new InvariantError("Problème de empty");
        // \inv isActivePrio(i) = i ∈ activePrios()
		for (Integer i : activePrios())
			if (!(isActivePrio(i)))
				throw new InvariantError("Problème d'activité");
        // \inv maxPrio() = max{activePrios()
		if (!(maxPrio() == Collections.max(activePrios())))
			throw new InvariantError("Priorité maximale non respectée");
        // \inv getPrio(i) = getElement(i, 1)
		for (Integer i : activePrios())
			if (!(getPrio(i) == getElem(i, 0)))
				throw new InvariantError("Erreur de premier élément");
        // \inv get() = getPrio(maxPrio())
		if (!(get() == getPrio(maxPrio())))
			throw new InvariantError("Erreur de get min= getPrio max");
        // \inv \forall i \in activePrios(), sizePrio(i) > 0
		for (Integer i : activePrios())
			if (!(sizePrio(i) > 0))
				throw new InvariantError("Erreur: prios actives avec taille 0");
        // \inv \forall i \notin activePrios(), sizePrio(i) == 0
        ArrayList<Integer> notin = new ArrayList<>();
        for (int i = 0; i < maxPrio(); i++)
            if (!isActivePrio(i))
                notin.add(i);
        for (Integer i : notin)
            if (!(sizePrio(i) == 0))
                throw new InvariantError("Prio non active non vide!");
        // \inv \forall i \in activePrios(), \forall k \in [1..sizePrio(i)], getElem(i, k) != null
		for (Integer i : activePrios())
			for (int k = 0; k < sizePrio(i); k++)
				if (!(getElem(i, k) != null))
					throw new InvariantError("Erreur: priorités nulles dans les listes");
	}
	
	@Override
	public int size() {
        // 3. Invariants
        checkInvariants();
        // 4. Métier
        int res = delegates.size();
        // 5. Invariants
        checkInvariants();
		return res;
	}

	@Override
	public boolean empty() {
        // 3. Invariants
        checkInvariants();
        // 4. Métier
        boolean res = delegates.empty();
        // 5. Invariants
        checkInvariants();
        return res;
	}

	@Override
	public Set<Integer> activePrios() {
        // 3. Invariants
        checkInvariants();
        // 4. Métier
        Set<Integer> res = delegates.activePrios();
        // 5. Invariants
        checkInvariants();
        return res;
	}

	@Override
	public boolean isActivePrio(int n) {
        // 3. Invariants
        checkInvariants();
        // 4. Métier
        boolean res = delegates.isActivePrio(n);
        // 5. Invariants
        checkInvariants();
        return res;
	}

	@Override
	public int maxPrio() {
        // 3. Invariants
        checkInvariants();
        // 4. Métier
        int res = delegates.maxPrio();
        // 5. Invariants
        checkInvariants();
        return res;
	}

	@Override
	public int sizePrio(int n) {
        // 3. Invariants
        checkInvariants();
        // 4. Métier
        int res = delegates.sizePrio(n);
        // 5. Invariants
        checkInvariants();
        return res;
	}

	@Override
	public T getPrio(int prio) {
        // 3. Invariants
        checkInvariants();
        // 4. Métier
        T res = delegates.getPrio(prio);
        // 5. Invariants
        checkInvariants();
        return res;
	}

	@Override
	public T get() {
        // 3. Invariants
        checkInvariants();
        // 4. Métier
        T res = delegates.get();
        // 5. Invariants
        checkInvariants();
        return res;
	}

	@Override
	public T getElem(int pri, int n) {
        // 3. Invariants
        checkInvariants();
        // 4. Métier
        T res = delegates.getElem(pri, n);
        // 5. Invariants
        checkInvariants();
        return res;
	}

	@Override
	public void init() {
        // 3. Invariants
        checkInvariants();
        // 4. Métier
        delegates.init();
        // 5. Invariants
        checkInvariants();
    }

	@Override
	public void putPrio(int prio, T elem) {
        // 3. Invariants
        checkInvariants();
        // 4. Métier
        delegates.putPrio(prio, elem);
        // 5. Invariants
        checkInvariants();
    }

	@Override
	public void put(T elem) {
        // 3. Invariants
        checkInvariants();
        // 4. Métier
        delegates.put(elem);
        // 5. Invariants
        checkInvariants();
    }

	@Override
	public void removePrio(int prio) {
        // 3. Invariants
        checkInvariants();
        // 4. Métier
        delegates.removePrio(prio);
        // 5. Invariants
        checkInvariants();
    }

	@Override
	public void remove(T elem) {
        // 3. Invariants
        checkInvariants();
        // 4. Métier
        delegates.remove(elem);
        // 5. Invariants
        checkInvariants();
    }
}
