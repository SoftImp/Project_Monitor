<template>
  <div>
    <h3>Add Strategic Goal</h3>
    <b-form @submit="onSubmit">

      <formgroupsg :sg="sg" ref="formaddsg"></formgroupsg>

      <b-button type="submit" variant="primary">Add</b-button> |
      <router-link to="/org">Back to List</router-link>
    </b-form>
  </div>
</template>

<script>
  var formgroupsg = httpVueLoader('components/formgroupsg.vue');

  module.exports = {
    data: function () {
      return {
        sg: { 
          name: null,
          description: null,
          priority: '',
        }
      }
    },
    methods: {
      async onSubmit(event) {
        event.preventDefault();
        await this.$refs.formaddsg.handleSubmit(null);
        this.$router.push({ name: 'org' });
      }
    },
    components: {
      'formgroupsg': formgroupsg
    }
  };
</script>