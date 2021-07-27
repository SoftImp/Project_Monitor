<template>
  <div class="row">
    <h3>Add Strategic Goal</h3>
    <div v-if="errors.length">
      <b>Please correct the following error(s):</b>
    <ul>
      <li v-for="error in errors">{{ error }}</li>
    </ul>
    </div>
    <div class="col-md-8">
      <form @submit.prevent="processForm">
        <div class="mb-3">
          <label for="name" class="form-label">Goal Identity</label>
          <input type="text" v-model="name" name="name" class="form-control" id="name">
        </div>
        <div class="mb-3">
          <label for="description" class="form-label">Description</label>
          <input type="text" v-model="description" name="description" class="form-control" id="description">
        </div>
        <div class="mb-3">
          <label for="priority" class="form-label">Priority</label>
          <select class="form-select" id="priority" v-model="priority" name="priority">
            <option value="Select" selected="true" disabled="disabled">Select Priority ...</option>
            <option value="HIGH">HIGH</option>
            <option value="MEDIUM">MEDIUM</option>
            <option value="LOW">LOW</option>
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
        errors: [],
        name: null,
        description: null,
        priority: 'Select'
      }
    },
    methods: {
      async processForm() {
        if (this.name && this.description && this.priority != 'Select') {
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
        else {
          this.errors = [];

          if (!this.name) {
            this.errors.push('Name required.');
          }
          if (!this.description) {
            this.errors.push('Description required.');
          }
          if (this.priority == 'Select') {
            this.errors.push('Priority required.');
          }
        }
      }
    }
  };
</script>