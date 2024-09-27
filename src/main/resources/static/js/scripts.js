$(document).ready(function() {
  $("#memberForm").submit(function(event) {
    event.preventDefault(); // Previene el comportamiento por defecto del formulario

    const memberData = {
      name: $("#name").val(),
      email: $("#email").val(),
      phoneNumber: $("#phoneNumber").val()
    };

    console.log(memberData);
    let dataReq = JSON.stringify(memberData);
    console.log(dataReq);

    $.ajax({
      type: "POST",
      url: "/kitchensink/register",
      contentType: "application/json",
      data: dataReq,
      success: function(response) {
        // Agregar el nuevo miembro a la lista sin recargar
        const newRow = `<tr>
                    <td>${response.id}</td>
                    <td>${response.name}</td>
                    <td>${response.email}</td>
                    <td>${response.phoneNumber}</td>
                </tr>`;
        $("#membersList").append(newRow);
        $("#message").html('<div class="alert alert-success">Registered!</div>');

        // Limpiar el formulario
        $("#memberForm")[0].reset();
      },
      error: function() {
        $("#message").html('<div class="alert alert-danger">Error registering member.</div>');
      }
    });
  });
});
