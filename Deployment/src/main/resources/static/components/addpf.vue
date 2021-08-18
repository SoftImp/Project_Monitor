<template>
  <div>
    <h3>Add Portfolio</h3>
    <b-form @submit="onSubmit">
      <formgrouppf :pf="pf" ref="formaddpf"></formgrouppf>
      <b-button type="submit" variant="primary">Add</b-button> |
      <router-link to="/pfm">Back to List</router-link>
    </b-form>
  </div>
</template>

<script>
  var formgrouppf = httpVueLoader('components/formgrouppf.vue');

  module.exports = {
    data: function () {
      return {
        pf: {
          name: null,
          description: null,
          manager: null,
          mission: null,
          vision: null,
          strategicGoal: '',
          programs: [],
          projects: [],
          currentState: 0
        }        
      }
    },
    methods: {
      async onSubmit(event) {
        event.preventDefault();
        await this.$refs.formaddpf.handleSubmit(null, 'add');
        this.$router.push({ name: 'pfm' });
      }
    },
    components: {
      'formgrouppf': formgrouppf
    }
  };
</script>