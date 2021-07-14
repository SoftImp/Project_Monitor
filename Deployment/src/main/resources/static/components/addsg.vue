<template>
  <div class="row">
    <h3>Add Strategic Goal</h3>
    <div class="col-md-8">
      <form>
        <div class="mb-3">
          <label for="goal_id" class="form-label">Goal Identity</label>
          <input type="text" class="form-control" id="goal_id">
        </div>
        <div class="mb-3">
          <label for="description" class="form-label">Description</label>
          <input type="text" class="form-control" id="description">
        </div>
        <div class="mb-3">
          <label for="priority" class="form-label">Priority</label>
          <select class="form-select" id="priority">
            <option selected="true" disabled="disabled">Select Priority ...</option>
            <option value="0">High</option>
            <option value="1">Medium</option>
            <option value="2">Low</option>
          </select>
        </div>
        <button type="submit" class="btn btn-primary">Add</button> | 
		<router-link to="/org">Back to List</router-link>
      </form>
    </div>
  </div>
</template>

<script>
  module.exports = {
    data: function () {
      return {
        who: 'world'
      }
    }
  };
</script>