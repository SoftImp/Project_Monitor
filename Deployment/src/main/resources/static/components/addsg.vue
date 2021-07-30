<template>
  <div>
    <h3>Add Strategic Goal</h3>
    <b-form @submit="onSubmit">
      <b-form-group id="input-group-1" label="Goal Identity:" label-for="input-1"
        description="Please enter a unique Goal Identity.">
        <b-form-input id="input-1" v-model="name" type="text" required></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-2" label="Description:" label-for="input-2">
        <b-form-input id="input-2" v-model="description" required></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-3" label="Priority:" label-for="input-3">
        <b-form-select id="input-3" v-model="priority" :options="priorities" required></b-form-select>
      </b-form-group>
      <b-button type="submit" variant="primary">Submit</b-button> |
      <router-link to="/org">Back to List</router-link>
    </b-form>
  </div>
</template>

<script>
  module.exports = {
    data: function () {
      return {
        name: null,
        description: null,
        priority: null,
        priorities: [{ text: 'Select Priority ...', value: null }, 'HIGH', 'MEDIUM', 'LOW']
      }
    },
    methods: {
      async onSubmit(event) {
        event.preventDefault();
        const response = await fetch('./addsg', {
          method: 'POST',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ goalId: this.name, description: this.description, priority: this.priority })
        });
        this.$router.push({ name: 'org' });
      }
    }
  };
</script>