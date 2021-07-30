<template>
  <div>
    <h3>Add Portfolio</h3>	
	<associate-sg></associate-sg>
    <div class="col-md-8">
      <form>
        <div class="mb-3">
          <label for="pf_name" class="form-label">Portfolio Name</label>
          <input type="text" class="form-control" id="pf_name">
        </div>
        <div class="mb-3">
          <label for="description" class="form-label">Description</label>
          <input type="text" class="form-control" id="description">
        </div>
		<div class="mb-3">
          <label for="manager" class="form-label">Manager</label>
          <input type="text" class="form-control" id="manager">
        </div>
		<div class="mb-3">
          <label for="st_goal" class="form-label">Strategic Goals</label>
		  <div class="input-group">
		    <input type="text" id="st_goal" class="form-control" disabled>
          	<!-- Button trigger modal -->
            <button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#exampleModal">
             Associate
             </button>
		  </div>
        </div>
		<div class="mb-3">
          <label for="programs" class="form-label">Programs</label>
		  <div class="input-group">
		    <input type="text" id="programs" class="form-control" disabled>
          	<!-- Button trigger modal -->
            <button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#exampleModal">
             Associate
             </button>
		  </div>
        </div>
		<div class="mb-3">
          <label for="projects" class="form-label">Projects</label>
		  <div class="input-group">
		    <input type="text" id="projects" class="form-control" disabled>
          	<!-- Button trigger modal -->
            <button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#exampleModal">
             Associate
             </button>
		  </div>
        </div>
        <button type="submit" class="btn btn-primary">Add</button> | 
		<router-link to="/pfm">Back to List</router-link>
      </form>
    </div>
  </div>
</template>

<script>  
  var associatesg = httpVueLoader('components/associatesg.vue');
  
  module.exports = {
    data: function () {
      return {
        who: 'world'
      }
    },
	components: {
            'associate-sg': associatesg
        }
  };
</script>