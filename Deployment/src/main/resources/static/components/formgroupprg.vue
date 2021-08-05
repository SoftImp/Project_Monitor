<template>
  <div>
    <b-form-group label="Program Name:" label-for="input-1" invalid-feedback="Name is required" :state="state.name">
        <b-form-input id="input-1" ref="name" v-model.trim="prg.name" type="text" :disabled="disabledName" :state="state.name" required></b-form-input>
    </b-form-group>

    <b-form-group label="Description:" label-for="input-2" invalid-feedback="Description is required" :state="state.desc">
        <b-form-input id="input-2" ref="description" v-model.trim="prg.description" :state="state.desc" required></b-form-input>
    </b-form-group>     

    <b-form-group label="Owner:" label-for="input-3" invalid-feedback="Owner is required" :state="state.owner">
        <b-form-input id="input-3" ref="owner" v-model.trim="prg.owner" :state="state.owner" required></b-form-input>
    </b-form-group>  

    <associate :assoc="assoc_sg"></associate>
    <associate :assoc="assoc_prj"></associate>
  </div>
</template>

<script>
  var associate = httpVueLoader('components/associate.vue');

  module.exports = {
    props: {
      prg: Object,
      disabledName: null  
    },
    data: function () {
      return {
        state: {
            name: null,
            desc: null,
            owner: null
        },
        assoc_sg: {
          id: 'modal-prg-associatesg',
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
        assoc_prj: {
          id: 'modal-prg-associateprj',
          createid: '',
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
      checkFormValidity() {
        this.state.name = this.$refs.name.checkValidity();
        this.state.desc = this.$refs.description.checkValidity();
        this.state.owner = this.$refs.owner.checkValidity();

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
          for (const s in this.prg) {
            if (typeof(this.prg[s]) == "object")
              this.prg[s] = [];
            else
              this.prg[s] = '';
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

        /*const response = await fetch('./addprg', {
          method: 'POST',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ name: this.name, description: this.description, manager: this.manager, 
            strategicGoal: strategicgoal, programs: this.assoc_prg.selected, projects: this.assoc_prj.selected})
        });*/

        this.$emit('ok', this.prg.name);  // Notify parent
        
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