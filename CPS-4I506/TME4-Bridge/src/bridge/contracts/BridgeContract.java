package bridge.contracts;

import bridge.services.BridgeService;

public class BridgeContract extends LimitedRoadContract implements BridgeService {

	public BridgeContract(BridgeService delegate) {
		super(delegate);
	}

	@Override
	protected BridgeService getDelegate() {
		return (BridgeService) super.getDelegate();
	}

	@Override
	public int getNbIn() {
		return getDelegate().getNbIn();
	}

	@Override
	public int getNbOut() {
		return getDelegate().getNbOut();
	}

	public void checkInvariant() {
		// TODO
		// raffinement donc
		if(getNbCars()!=(getNbIn()+getNbOut()))
			Contractor.defaultContractor().invariantError("BridgeContract","The number of cars should be equal to NbIn+NbOut");
		if(getNbIn()<0)
			Contractor.defaultContractor().invariantError("BridgeContract","getNbIn should be positive");
		if(getNbOut()<0)
			Contractor.defaultContractor().invariantError("BridgeContract","getNbOut should be positive");
		super.checkInvariant();
	}


	@Override
	public void init() {
		// TODO
		getDelegate().init();
	}

	@Override
	public void init(int lim) {
		if(!(lim>0))
			Contractor.defaultContractor().preconditionError("BridgeContract","init","Limite should be greater than 0");
		getDelegate().init(lim);
		// \post : getNbIn() == 0
		if(!(getNbIn() == 0))
			Contractor.defaultContractor().postconditionError("BridgeContract","ini"," nbIn should be 0 at initialisation");
		// \post : getNbOut() == 0
		if(!(getNbOut() == 0))
			Contractor.defaultContractor().postconditionError("BridgeContract","ini","nbOut should be 0 at initialisation");
		// \post : getLimit() == lim
		if(!(lim == getLimit()))
			Contractor.defaultContractor().postconditionError("BridgeContract","init","Limite should be equal to parameter lim at initialisation");
	}

	@Override
	public void enterIn() {
		// TODO
		// \ pre : !isFull()
		if(isFull())
			Contractor.defaultContractor().preconditionError("BridgeContract","enterIn","Bridge FULL");
		checkInvariant();
		//cap
		int NbIn_pre = getNbIn();
		int NbOut_pre = getNbOut();
		getDelegate().enterIn();
		checkInvariant();
		// \post : getNbIn() == getNbIn()@pre + 1
		if(!(getNbIn() == (NbIn_pre+1)))
			Contractor.defaultContractor().postconditionError("BridgeContract","enterIn","NbIn hasn't changed");
		// \post : getNbOut() == getNbOut()@pre
		if(!(getNbOut() == (NbOut_pre)))
			Contractor.defaultContractor().postconditionError("BridgeContract","enterIn","NbOut has changed");
	}

	@Override
	public void leaveIn() {
		// TODO
		//pre getNbIn()>0
		if(getNbIn()<=0)
			Contractor.defaultContractor().preconditionError("BridgeContract","leaveIn","Bridge Empty or negative");
		checkInvariant();
		//cap
		int NbIn_pre = getNbIn();
		int NbOut_pre = getNbOut();
		getDelegate().leaveIn();
		checkInvariant();
		// \post : getNbIn() == getNbIn()@pre - 1
		if(!(getNbIn() == (NbIn_pre-1)))
			Contractor.defaultContractor().postconditionError("BridgeContract","leaveIn","NbIn hasn't changed");
		// \post : getNbOut() == getNbOut()@pre
		if(!(getNbOut() == (NbOut_pre)))
			Contractor.defaultContractor().postconditionError("BridgeContract","leaveIn","NbOut has changed");
	}

	@Override
	public void enterOut() {
		// \ pre : !isFull()
		if(isFull())
			Contractor.defaultContractor().preconditionError("BridgeContract","enterOut","Bridge FULL");
		checkInvariant();
		//cap
		int NbIn_pre = getNbIn();
		int NbOut_pre = getNbOut();
		getDelegate().enterOut();
		// \post : getNbIn() == getNbIn()@pre
		if(!(getNbIn() == (NbIn_pre)))
			Contractor.defaultContractor().postconditionError("BridgeContract","enterOut","NbIn has changed");
		// \post : getNbOut() == getNbOut()@pre +1
		if(!(getNbOut() == (NbOut_pre+1)))
			Contractor.defaultContractor().postconditionError("BridgeContract","enterOut","NbOut hasn't changed");
	}

	@Override
	public void leaveOut() {
		//TODO
		//pre getNbOut()>0
		if(getNbOut()<=0)
			Contractor.defaultContractor().preconditionError("BridgeContract","leaveOut","Bridge Empty or negative");
		checkInvariant();
		//cap
		int NbIn_pre = getNbIn();
		int NbOut_pre = getNbOut();
		getDelegate().leaveOut();
		// \post : getNbIn() == getNbIn()@pre
		if(!(getNbIn() == (NbIn_pre)))
			Contractor.defaultContractor().postconditionError("BridgeContract","leaveOut","NbIn has changed");
		// \post : getNbOut() == getNbOut()@pre -1
		if(!(getNbOut() == (NbOut_pre-1)))
			Contractor.defaultContractor().postconditionError("BridgeContract","leaveOut","NbOut hasn't changed");
	}

}
