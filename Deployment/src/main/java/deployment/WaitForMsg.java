package deployment;

public class WaitForMsg {
	private String msg;
	boolean wasSignalled;
	
	public WaitForMsg() {
		clear();
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
	
	public void setSignalled(boolean signalled) {
		wasSignalled = signalled;
	}
	
	public boolean getSignalled() {
		return wasSignalled;
	}
	
	public void clear() {
		wasSignalled = false;
		msg = "";
	}
	
	public boolean hasMsg() {
		// Ignore empty array []
		return (msg.length() > 2 ? true : false);
	}
	
	public boolean synchroniseAndWait() {
		synchronized(this) {
			while (!getSignalled()) {
				try{
					wait();
				} 
				catch(InterruptedException e) {
					System.out.printf("InterruptedException, %s, in synchroniseAndWait()\n", e);
					return false;
				}
			}
			//clear signal and continue running.
			setSignalled(false);
			return true;
		}
	}

	public void onNotify(String msg) {	
		synchronized(this){
			setMsg(msg);
			setSignalled(true);
			notify();
		}
	}
}