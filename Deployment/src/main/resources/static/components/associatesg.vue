<template>
  <!-- Modal -->
  <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">Strategic Goals</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <table class="table table-striped">
            <thead>
              <tr>
                <th>Goal Identity</th>
                <th>Description</th>
                <th>Priority</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>
                  <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="" id="flexCheckChecked">
                    <label class="form-check-label" for="flexCheckChecked">
                      Goal1
                    </label>
                  </div>
                </td>
                <td>Goal1 description</td>
                <td>High</td>
              </tr>
              <tr>
                <td>
                  <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="" id="flexCheckChecked2">
                    <label class="form-check-label" for="flexCheckChecked2">
                      Goal2
                    </label>
                  </div>
                </td>
                <td>Goal2 description</td>
                <td>Medium</td>
              </tr>
              <tr>

                <td>
                  <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="" id="flexCheckChecked3">
                    <label class="form-check-label" for="flexCheckChecked3">
                      Goal3
                    </label>
                  </div>
                </td>
                <td>Goal3 description</td>
                <td>Low</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary">Associate</button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>