<template>
  <div>
    <b-form-group label="Project Name:" label-for="input-1" invalid-feedback="Name is required" :state="state.name">
      <template v-if="mode === 'add'">
        <b-form-input id="input-1" ref="name" v-model.trim="prj.name" :state="state.name" required></b-form-input>
      </template>
      <template v-else>
        <b-form-input id="input-1" ref="name" :value="prj.name" readonly="true" :state="state.name" required></b-form-input>
      </template>
    </b-form-group>

    <b-form-group label="Description:" label-for="input-2" invalid-feedback="Description is required" :state="state.desc">
      <!--<b-form-input id="input-2" ref="description" v-model.trim="prj.description" :state="state.desc" required></b-form-input>-->
      <b-form-textarea id="input-2" ref="description" v-model.trim="prj.description" :state="state.desc"></b-form-textarea>
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
      mode: {
        type: String,
        default: 'add'
      }
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
          title: 'Associate Program',
          label: 'Program:',
          url: './getprg',
          mode: 'single',
          selected: [],
          fields: [
            'selected',
            { key: 'name', label: 'Program' },
            { key: 'description' },
            { key: 'owner' }
          ]
        }
      }
    },
    created() {
      if (this.mode === 'update') {       
        this.assoc_sg.selected.push(this.prj.strategicGoal);
        this.assoc_prg.selected.push(this.prj.program);
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

        this.prj.strategicGoal = (this.assoc_sg.selected.length > 0) ? this.assoc_sg.selected[0] : "";
        this.prj.program = (this.assoc_prg.selected.length > 0) ? this.assoc_prg.selected[0] : "";

        let url = './addprj';
        if (this.mode === 'update')
          url = '/updateprj';

        const response = await fetch(url, {
          method: 'POST',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.prj)
        });

        this.$emit('ok', this.prj.name);  // Notify parent

        if (modalId) {
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