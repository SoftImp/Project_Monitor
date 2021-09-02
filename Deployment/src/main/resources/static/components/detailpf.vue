<template>
  <div>
    <b-card :title="pf.name"> <!-- :sub-title="status">-->
      <b-row>
        <b-col md="2" class="mb-2">
          <b-card-text>Status:</b-card-text>
        </b-col>
        <b-col md="10">
          <!--<b-card-text><strong>{{pf_state[pf.currentState]}}</strong></b-card-text>-->
          <b-button size="sm" @click="update($event.target)">
            {{pf_state[pf.currentState]}}
          </b-button>
        </b-col>
        <b-col md="2" class="mb-2">
          <b-card-text>Manager:</b-card-text>
        </b-col>
        <b-col md="10">
          <b-card-text>{{pf.manager}}</b-card-text>
        </b-col>
        <b-col md="2" class="mb-2">
          <b-card-text>
            Priority:
          </b-card-text>
        </b-col>
        <b-col md="10">
          <b-card-text><b-badge pill class="px-4" :variant="priority_variant[priorities.indexOf(pf.priority)]">{{pf.priority}}</b-badge></b-card-text>
        </b-col>
        <b-col md="2" class="mb-2">
          <b-card-text>Budget:</b-card-text>
        </b-col>
        <b-col md="10">
          <b-card-text>${{pf.budget}}</b-card-text>
        </b-col>
        <b-col md="2" class="mb-2">
          <b-card-text>Description:</b-card-text>
        </b-col>
        <b-col md="10">
          <b-card-text><pre>{{pf.description}}</pre></b-card-text>
        </b-col>
        <b-col md="2" class="mb-2">
          <b-card-text>Mission:</b-card-text>
        </b-col>
        <b-col md="10">
          <b-card-text><pre>{{pf.mission}}</pre></b-card-text>
        </b-col>
        <b-col md="2" class="mb-2">
          <b-card-text>Vision:</b-card-text>
        </b-col>
        <b-col md="10">
          <b-card-text><pre>{{pf.vision}}</pre></b-card-text>
        </b-col>
      </b-row>
      <b-card sub-title="Component associations">
        <b-row>
          <b-col md="3" class="mb-2">
            <b-card-text>Strategic Goal:</b-card-text>
          </b-col>
          <b-col md="9">
            <div><h5><b-badge variant="info" role="button" @click="showAssoc(pf.strategicGoal, 'sg')">{{pf.strategicGoal}}</b-badge></h5></div>
          </b-col>
          <b-col md="3" class="mb-2">
            <b-card-text>Programs:</b-card-text>
          </b-col>
          <b-col md="9">
            <div><h5><b-badge variant="info" role="button" class="mr-2" v-for="prg in pf.programs" @click="showAssoc(prg, 'prg')">{{prg}}</b-badge></h5></div>
          </b-col>
          <b-col md="3" class="mb-2">
            <b-card-text>Projects:</b-card-text>
          </b-col>
          <b-col md="9">
            <div><h5><b-badge variant="info" role="button" class="mr-2" v-for="prj in pf.projects" @click="showAssoc(prj, 'prj')">{{prj}}</b-badge></h5></div>
          </b-col>
        </b-row>
      </b-card>
    </b-card>

    <b-modal size="lg" :id="assoc_info.id"  :title="assoc_info.title" ok-only>
      <detailassoc :assoc="assoc_info"></detailassoc>
    </b-modal>
  </div>
</template>

<script>
  var detailassoc = httpVueLoader('components/detailassoc.vue'); 

  module.exports = {
    props: {
      pf: Object
    },
    data: function () {
      return {
        assoc_info: {
          title: 'Associated',
          id: 'modal-detailassoc-',
          type: '',
          selected: [],
        }
      }
    },
    created() {
      this.assoc_info.id += this.pf.name;
    },
    computed: {
      status: function () {
        return /*'Status: ' +*/ pf_state[this.pf.currentState];
      }
    },
    methods: {
      showAssoc(selected, type) {
        this.assoc_info.type = type;
        this.assoc_info.selected = selected;

        this.$root.$emit('bv::show::modal', this.assoc_info.id);
      },
      update(button) {
        this.$emit('update', this.pf, 0, button);  // Notify parent
      }
    },
    components: {
      'detailassoc': detailassoc
    }
  };
</script>