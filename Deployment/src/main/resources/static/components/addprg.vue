<template>
  <div class="row">
    <h3>Add Program</h3>
    <associate-sg></associate-sg>
    <div class="col-md-8">
      <form>
        <div class="mb-3">
          <label for="pf_name" class="form-label">Name</label>
          <input type="text" class="form-control" id="pf_name">
        </div>
        <div class="mb-3">
          <label for="st_goal" class="form-label">Strategic Goals</label>
          <div class="input-group">
            <input type="text" id="st_goal" class="form-control" disabled>
            <!-- Button trigger modal -->
            <button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal"
              data-bs-target="#exampleModal">
              Associate
            </button>
          </div>
        </div>
		<div class="mb-3">
          <label for="resource" class="form-label">Resource</label>
		  <div class="input-group">
		    <input type="text" id="resource" class="form-control" disabled>
          	<!-- Button trigger modal -->
            <button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#exampleModal">
             Allocate
             </button>
		  </div>
        </div>
        <div class="mb-2">
          <label for="requirement" class="form-label">Requirement</label>
          <div class="input-group">
            <input type="text" class="form-control" v-model="requirement">
            <!-- Button trigger modal -->
            <button type="button" id="enter" class="btn btn-outline-secondary" v-on:click="addRequirement">
              Add
            </button>
          </div>
        </div>
		<div class="mb-3">
            <ul class="list-group list-group-flush list-group-numbered">
			  <li class="list-group-item" v-for="requirement in requirements">{{ requirement }}</li>
            </ul>
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
        <router-link to="/prg">Back to List</router-link>
      </form>
    </div>
  </div>
</template>

<script>
  var associatesg = httpVueLoader('components/associatesg.vue');

  module.exports = {
    data: function () {
      return {
	    requirements: []
	  }
    },
	methods: {
     async addRequirement() {   
	    if (this.requirement != null && this.requirement != "")
		{
          this.requirements.push(this.requirement);
		  this.requirement = "";
		}  
     }
    },
    components: {
      'associate-sg': associatesg
    }
  };
</script>