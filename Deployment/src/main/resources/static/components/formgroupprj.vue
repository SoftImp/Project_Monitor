<template>
  <div>
    <b-form-group label="Project Name:" label-for="input-1" invalid-feedback="Name is required" :state="state.name">
        <b-form-input id="input-1" ref="name" v-model.trim="prj.name" type="text" :disabled="disabledName" :state="state.name" required></b-form-input>
    </b-form-group>

    <b-form-group label="Description:" label-for="input-2" invalid-feedback="Description is required" :state="state.desc">
        <b-form-input id="input-2" ref="description" v-model.trim="prj.description" :state="state.desc" required></b-form-input>
    </b-form-group>     

  <associate :assoc="assoc_sg"></associate>
  <associate :assoc="assoc_prg"></associate>

  </div>
</template>

<script>
  var associate = httpVueLoader('components/associate.vue');

  module.exports = {
    props: {
      prj: Object,
      disabledName: null  
    },
    data: function () {
      return {
        state: {
            name: null,
            desc: null
        },
        assoc_sg: {
          id: 'modal-prj-associatesg',
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
          id: 'modal-prj-associateprg',
          createid: '',
          title: 'Associate Programs',
          label: 'Programs:',
          url: './getsg',
          mode: 'single',
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
      checkFormValidity() {
        this.state.name = this.$refs.name.checkValidity();
        this.state.desc = this.$refs.description.checkValidity();

        for (const s in this.state) {
          if (this.state[s] == false)
            return false;
        }

        return true;
      },
      resetFormValidity(value) {
        for (const s in this.state) {
          this.state[s] = null;
        }

        if (value) {
          for (const s in this.prj) {
            if (typeof(this.prj[s]) == "object")
              this.prj[s] = [];
            else
              this.prj[s] = '';
          }
        }
      },
      handleOk(bvModalEvt, modalId) {
        // Prevent modal from closing
        if (bvModalEvt)
          bvModalEvt.preventDefault();
        // Trigger submit handler
        this.handleSubmit(modalId);
      },
      async handleSubmit(modalId) {
        if (!this.checkFormValidity()) {
          return;
        }

        /*const response = await fetch('./addprj', {
          method: 'POST',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ name: this.name, description: this.description, manager: this.manager, 
            strategicGoal: strategicgoal, programs: this.assoc_prj.selected, projects: this.assoc_prj.selected})
        });*/

        this.$emit('ok', this.prj.name);  // Notify parent

        if (modalId) {
          this.resetFormValidity(true);
          // Hide the modal manually
          this.$nextTick(() => {
            this.$bvModal.hide(modalId)
          })
        }
      }
    },
    components: {
      'associate': associate
    }
  };
</script>