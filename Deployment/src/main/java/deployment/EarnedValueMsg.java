package deployment;

// Spring requires a POJO class for each message - variables must start with a lower case (camel case convention)
public class EarnedValueMsg extends AbstractMsg {
	private int repId;
	private double bac;
	private double ev;
	private double pv;
	private double ac;

	@Override
	public String getId() {
		return Integer.toString(repId);
	}

	public EarnedValueMsg() {
		repId = 0;
		bac = 0d;
		ev = 0d;
		pv = 0d;
		ac = 0d;
	}

	public EarnedValueMsg(int repId, double bac, double ev, double pv, double ac) {
		this.repId = repId;
		this.bac = bac;
		this.ev = ev;
		this.pv = pv;
		this.ac = ac;
	}

	public void setRepId(int repId) {
		this.repId = repId;
	}
	
	public int getRepId() {
		return repId;
	}

	public void setBac(double bac) {
		this.bac = bac;
	}
	
	public double getBac() {
		return bac;
	}
	
	public void setEv(double ev) {
		this.ev = ev;
	}
	
	public double getEv() {
		return ev;
	}

	public void setPv(double pv) {
		this.pv = pv;
	}
	
	public double getPv() {
		return pv;
	}
	
	public void setAc(double ac) {
		this.ac = ac;
	}
	
	public double getAc() {
		return ac;
	}
}
