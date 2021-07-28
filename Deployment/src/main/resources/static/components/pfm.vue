<template>
  <div>
    <h3>Portfolio Management</h3>
    <p>
      <router-link to="/pfm/addpf">Add</router-link>
    </p>
    <table class="table table-striped">
      <thead>
        <tr>
          <th>Portfolio</th>
          <th>Description</th>
          <th>Manager</th>
		  <th>Strategic Goal</th>
		  <th>Program</th>
		  <th>Project</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>Portfolio1</td>
          <td>Portfolio1 description</td>
          <td>Joe Blow</td>
		  <td>Goal1<br></td>
		  <td></td>
		  <td></td>
          <td><a href="#">Update</a></td>
        </tr>
		<tr>
          <td>Portfolio2</td>
          <td>Portfolio2 description</td>
          <td>John Smith</td>
		  <td></td>
		  <td></td>
		  <td></td>
          <td><a href="#">Update</a></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
  module.exports = {
    data: function () {
      return {
        who: 'world'
      }
    }
  }
</script>