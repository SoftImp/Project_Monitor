package deployment;

// Spring requires a POJO class for each message - variables must start with a lower case (camel case convention)
public class PerfRepMsg extends EarnedValueMsg {
  private String project;
  private String trafficLight;
  private double cv;
  private double sv;
  private double cpi;
  private double spi;
  private double eac;
  private double vac;
  private double etc;
	
  public PerfRepMsg(){
    super();
	  project = "";
    trafficLight = "";
    cv = 0d;
    sv = 0d;
    cpi = 0d;
    spi = 0d;
    eac = 0d;
    vac = 0d;
    etc = 0d;
  }
  	
  public void setProject(String project) {
    this.project = project;
  }

  public String getProject() {
    return project == null ? "" : project;
  }

  public void setTrafficLight(String trafficLight) {
    this.trafficLight = trafficLight;
  }

  public String getTrafficLight() {
    return trafficLight == null ? "" : trafficLight;
  }

  public void setCv(double cv) {
		this.cv = cv;
	}
	
	public double getCv() {
		return cv;
	}

  public void setSv(double sv) {
		this.sv = sv;
	}
	
	public double getSv() {
		return sv;
	}

  public void setCpi(double cpi) {
		this.cpi = cpi;
	}
	
	public double getCpi() {
		return cpi;
	}

  public void setSpi(double spi) {
		this.spi = spi;
	}
	
	public double getSpi() {
		return spi;
	}

  public void setEac(double eac) {
		this.eac = eac;
	}
	
	public double getEac() {
		return eac;
	}

  public void setVac(double vac) {
		this.vac = vac;
	}
	
	public double getVac() {
		return vac;
	}

  public void setEtc(double etc) {
		this.etc = etc;
	}
	
	public double getEtc() {
		return etc;
	}
}