<template>
  <div>
    <h3>Add Project</h3>
    <b-form @submit="onSubmit">

      <formgroupprj :prj="prj" ref="formaddprj"></formgroupprj>

      <b-button type="submit" variant="primary">Add</b-button> |
      <router-link to="/prj">Back to List</router-link> 
    </b-form>
  </div>
</template>

<script>
  var formgroupprj = httpVueLoader('components/formgroupprj.vue');

  module.exports = {
    data: function () {
      return {
        prj: {
          name : '',
          description: '',
          strategicGoal: '',
          program: '',
          portfolio: ''
        }
	    }
    },
	  methods: {
      async onSubmit(event) {
        event.preventDefault();
        await this.$refs.formaddprj.handleSubmit(null);
        this.$router.push({ name: 'prj' });
      }
     },
    components: {
      'formgroupprj': formgroupprj
    }
  };
</script>