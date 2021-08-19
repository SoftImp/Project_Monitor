<template>
  <div>
    <b-card :title="pf.name"> <!-- :sub-title="status">-->
      <b-row>
        <b-col md="2" class="mb-2">
          <b-card-text>Status:</b-card-text>
        </b-col>
        <b-col md="10">
          <b-card-text><strong>{{pf_state[pf.currentState]}}</strong></b-card-text>
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
            <div><h5><b-badge variant="info">{{pf.strategicGoal}}</b-badge></h5></div>
          </b-col>
          <b-col md="3" class="mb-2">
            <b-card-text>Programs:</b-card-text>
          </b-col>
          <b-col md="9">
            <div><h5><b-badge variant="info" class="mr-2" v-for="prg in pf.programs">{{prg}}</b-badge></h5></div>
          </b-col>
          <b-col md="3" class="mb-2">
            <b-card-text>Projects:</b-card-text>
          </b-col>
          <b-col md="9">
            <div><h5><b-badge variant="info" class="mr-2" v-for="prj in pf.projects">{{prj}}</b-badge></h5></div>
          </b-col>
        </b-row>
      </b-card>
    </b-card>
  </div>
</template>

<script>
  module.exports = {
    props: {
      pf: Object
    },
    computed: {
      status: function () {
        return /*'Status: ' +*/ pf_state[this.pf.currentState];
      }
    }
  };
</script>