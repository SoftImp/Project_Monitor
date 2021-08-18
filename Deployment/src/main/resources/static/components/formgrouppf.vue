<template>
  <div>
    <template v-if="pf_state[pf.currentState] === 'Closed'">
      <detailpf :pf="pf"></detailpf> 
    </template>
    <template v-else>
      <b-form-group id="input-group-1" label="Portfolio Name:" label-for="input-1" :state="state.name">
        <template v-if="mode === 'add'">
          <b-form-input id="input-1" ref="name" v-model.trim="pf.name" :state="state.name" required></b-form-input>
        </template>
        <template v-else>
            <b-form-input id="input-1" ref="name" :value="pf.name" readonly="true" :state="state.name" required></b-form-input>
        </template>
      </b-form-group>

      <b-form-group id="input-group-3" label="Manager:" label-for="input-3" :state="state.manager">
        <template v-if="mode === 'add' || mode === 'Planning' || mode === 'Executing'">
          <b-form-input id="input-3" ref="manager" v-model.trim="pf.manager" :state="state.manager" required></b-form-input>
        </template>
        <template v-else>
          <b-form-input id="input-3" ref="manager" :value="pf.manager" readonly="true" :state="state.manager" required></b-form-input>
        </template>  
      </b-form-group>

      <template v-if="mode === 'add' || mode === 'Initialise'">
        <associate :assoc="assoc_sg"></associate>
      
        <b-form-group id="input-group-2" label="Description:" label-for="input-2" :state="state.desc">
          <!--<b-form-input id="input-2" ref="description" v-model.trim="pf.description" :state="state.desc" required></b-form-input>-->
          <b-form-textarea id="input-2" ref="description" v-model.trim="pf.description" :state="state.desc" required ></b-form-textarea>
        </b-form-group>
        
        <b-form-group id="input-group-4" label="Mission:" label-for="input-4">
          <b-form-textarea id="input-4" ref="mission" v-model.trim="pf.mission"></b-form-textarea>
        </b-form-group>

        <b-form-group id="input-group-5" label="Vision:" label-for="input-5">
          <b-form-textarea id="input-5" ref="vision" v-model.trim="pf.vision"></b-form-textarea>
        </b-form-group>
      </template> 

      <template v-if="mode === 'Planning'">
        <b-form-group id="input-group-6" label="Priority:" label-for="input-6" :state="state.priority">
          <b-form-select id="input-6" ref="priority" v-model="pf.priority" :options="priorities" :state="state.priority" required></b-form-select>
        </b-form-group>
        <b-form-group id="input-group-7" label="Budget:" label-for="input-7" :state="state.budget">
          <b-input-group prepend="$" class="mb-2 mr-sm-2 mb-sm-0">
            <b-form-input id="input-7" ref="budget" v-model.trim="pf.budget" type="number" step="any" min="0" :state="state.budget" required></b-form-input>
          </b-input-group>  
        </b-form-group>
        <associate :assoc="assoc_prg"></associate>
        <associate :assoc="assoc_prj"></associate>
      </template>
    </template>
  </div>
</template>

<script>
  var associate = httpVueLoader('components/associate.vue');
  var detailpf = httpVueLoader('components/detailpf.vue');

  module.exports = {
    props: {
      pf: Object,
      mode: {
        type: String,
        default: 'add'
      }
    },
    data: function () {
      return {
        state: {
            name: null,
            desc: null,
            manager: null,
            budget: null
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
            { key: 'owner', sortable: true }
          ]
        },
        assoc_prj: {
          id: 'modal-prg-associateprj',
          createid: 'modal-createprj',
          title: 'Associate Projects',
          label: 'Projects:',
          url: './getprj',
          mode: 'multi',
          selected: [],
          fields: [
            'selected',
            { key: 'name', label: 'Project' },
            { key: 'description' }
          ]
        }
      }
    },
    created() {
      this.assoc_sg.selected.push(this.pf.strategicGoal);
      this.assoc_prg.selected = this.pf.programs;
      this.assoc_prj.selected = this.pf.projects;
    },
    methods: { 
      checkFormValidity() {
        if (this.mode === 'Closed')
          return true;

        this.state.name = this.$refs.name.checkValidity();
        this.state.manager = this.$refs.manager.checkValidity();

        if (this.mode === 'add' || this.mode === 'Initialise')
          this.state.desc = this.$refs.description.checkValidity();
        
        if (this.mode === 'Planning') {
          this.state.budget = this.$refs.budget.checkValidity();
          this.state.priority = (this.pf.priority == '') ? false : true;
        }

        for (const s in this.state) {
          if (this.state[s] == false)
            return false;
        }

        return true;
      },
      handleOk(bvModalEvt, modalId, action) {
        // Prevent modal from closing
        if (bvModalEvt)
          bvModalEvt.preventDefault();
        // Trigger submit handler
        this.handleSubmit(modalId, action);
      },
      async handleSubmit(modalId, action) {
        if (!this.checkFormValidity()) {
          return;
        }

        this.pf.strategicGoal = (this.assoc_sg.selected.length > 0) ? this.assoc_sg.selected[0] : this.pf.strategicGoal = "";
        this.pf.programs = this.assoc_prg.selected;
        this.pf.projects = this.assoc_prj.selected;

        //let url = (this.mode === 'add') ? '/addpf' : './updatepf';
        let url = './updatepf';

        if (action === 'add')
          url = './addpf';
        else
          url = './updatepf?action=' + action;

        const response = await fetch(url, {
          method: 'POST',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.pf)
        });

        this.$emit('ok', this.pf.name);  // Notify parent
        
        if (modalId) {
          // Hide the modal manually
          this.$nextTick(() => {
            this.$bvModal.hide(modalId)
          })
        }
      }
    },
    components: {
      'associate': associate,
      'detailpf': detailpf
    }
  };
</script>