package deployment;

import java.util.List;
import java.util.Arrays;
import com.google.gson.Gson;

public class TableData<T extends AbstractMsg> {
	private List<T> data;

	public TableData() {
	}

	public void setData(String json, Class<T[]> clazz) {
		this.data = Arrays.asList(new Gson().fromJson(json, clazz));
	}

	public List getData() {
		return data;
	}

	public T findById(final String id) {
		return data.stream().filter(p -> p.getId().equals(id)).findAny().orElse(null);
	}
}