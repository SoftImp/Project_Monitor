<template>
  <div>
    <h3>Add Portfolio</h3>
    <b-form @submit="onSubmit">
      <b-form-group id="input-group-1" label="Portfolio Name:" label-for="input-1"
        description="Please enter a unique Portfolio Name.">
        <b-form-input id="input-1" v-model.trim="name" type="text" required></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-2" label="Description:" label-for="input-2">
        <b-form-input id="input-2" v-model.trim="description" required></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-3" label="Manager:" label-for="input-3">
        <b-form-input id="input-3" v-model.trim="manager" required></b-form-input>
      </b-form-group>
      
      <associate :assoc="assoc_sg"></associate>
      <associate :assoc="assoc_prg"></associate>
      <associate :assoc="assoc_prj"></associate>

      <b-button type="submit" variant="primary">Add</b-button> |
      <router-link to="/pfm">Back to List</router-link>
    </b-form>
  </div>
</template>

<script>
  var associate = httpVueLoader('components/associate.vue');

  module.exports = {
    data: function () {
      return {
        name: null,
        description: null,
        manager: null,
        goals: null,
        assoc_sg: {
          id: 'modal-associatesg',
          createid: '',
          title: 'Associate Strategic Goal',
          label: 'Strategic Goal:',
          url: './getsg',
          mode: 'single',
          selected: [],
          fields: [
            'selected',
            { key: 'name', label: 'Goal Identity' },
            { key: 'description' },
            { key: 'priority' }
          ]
        },
        assoc_prg: {
          id: 'modal-associateprg',
          createid: 'modal-createprg',
          title: 'Associate Programs',
          label: 'Programs:',
          url: './getprg',
          mode: 'multi',
          selected: [],
          fields: [
            'selected',
            { key: 'name', label: 'Program', sortable: true },
            { key: 'description', sortable: false },
            { key: 'owner', sortable: true },
          ]
        },
        assoc_prj: {
          id: 'modal-associateprj',
          createid: 'modal-createprj',
          title: 'Associate Projects',
          label: 'Projects:',
          url: './getsg',
          mode: 'multi',
          selected: [],
          fields: [
            'selected',
            { key: 'name', label: 'Goal Identity' },
            { key: 'description' },
            { key: 'priority' }
          ]
        }
      }
    },
    methods: {
      async onSubmit(event) {
        event.preventDefault();

        let strategicgoal = '';
        if (this.assoc_sg.selected.length > 0)
          strategicgoal = this.assoc_sg.selected[0];

        const response = await fetch('./addpf', {
          method: 'POST',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ name: this.name, description: this.description, manager: this.manager, 
            strategicGoal: strategicgoal, programs: this.assoc_prg.selected, projects: this.assoc_prj.selected})
        });
        this.$router.push({ name: 'pfm' });
      }
    },
    components: {
      'associate': associate
    }
  };
</script>