package bridge.contracts;

import bridge.decorators.LimitedRoadDecorator;
import bridge.services.LimitedRoadService;

public class LimitedRoadContract extends LimitedRoadDecorator {

	public LimitedRoadContract(LimitedRoadService delegate) {
		super(delegate);
	}

	public void checkInvariant() {
		// remarque : include et non refine donc on n'hérite
		// pas des invariants de RoadSectionService, il faut refaire des tests.
				
		/* A COMPLETER */
		if(!(getNbCars()>=0)){
			Contractor.defaultContractor().invariantError("LimitedRoadContract", "The number of cars should be positive");
		}
		if(!(this.isFull()==(this.getNbCars()==this.getLimit())))
			Contractor.defaultContractor().invariantError("LimitedRoadContract","The number of cars should be equal to the limit if road full");
		if(this.getNbCars()>this.getLimit())
			Contractor.defaultContractor().invariantError("LimitedRoadContract","The number of cars should not be > to the limit");
	}
	
	/* A COMPLETER */
	@Override
	public void enter() {
		// \ pre : !isFull()
		if(this.isFull()) {
			Contractor.defaultContractor().preconditionError("LimitedRoadContract", "leave", "The number of cars is full");
		}
		// captures
		int getNbCars_atPre = getNbCars();
		// inv pre
		checkInvariant();
		// run
		super.enter();
		// int post
		checkInvariant();
		// post: getNbCars() == getNbCars()@pre + 1 
		if(!(getNbCars() == getNbCars_atPre + 1)) {
			Contractor.defaultContractor().postconditionError("LimitedRoadContract", "enter", "The cars count did not increase");
		}
	}
	
	@Override
	public void leave() {
		// pre: getNbCars() > 0
		if(!(getNbCars() > 0)) {
			Contractor.defaultContractor().postconditionError("LimitedRoadContract", "leave", "The number of cars is not strictly positive");
		}
		// captures
		int getNbCars_atPre = getNbCars();
		// inv pre
		checkInvariant();
		// run
		super.leave();
		// int post
		checkInvariant();
		// post: getNbCars() == getNbCars()@pre - 1 
		if(!(getNbCars() == getNbCars_atPre - 1)) {
			Contractor.defaultContractor().postconditionError("LimitedRoadContract", "leave", "The cars count did not decrease");
		}
	}
	
}
