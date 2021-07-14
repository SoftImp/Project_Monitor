<template>
  <div>
    <h3>Program Management</h3>
    <p>
      <router-link to="/prg/addprg">Add</router-link>
    </p>
    <table class="table table-striped">
      <thead>
        <tr>
          <th>Program</th>
          <th>Description</th>
		  <th>Associated<br>Strategic Goal</th>
		  <th>Allocated<br>Resource</th>
		  <th>Requirement</th>
		  <th>Associated<br>Project</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>Program1</td>
          <td>Program1 description</td>
          <td>Goal1</td>
		  <td></td>
		  <td></td>
		  <td></td>
          <td><a href="#">Update</a></td>
        </tr>
		<tr>
          <td>Program2</td>
          <td>Program2 description</td>
          <td>Goal1</td>
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