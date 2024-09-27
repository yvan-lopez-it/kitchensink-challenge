$(document).ready(function () {
  $("#memberForm").submit(function (event) {
    event.preventDefault(); // Previene el comportamiento por defecto del formulario

    // Obtener los valores del formulario
    const memberData = {
      name: $("#name").val(),
      email: $("#email").val(),
      phoneNumber: $("#phoneNumber").val()
    };

    // Validar el nombre (no debe contener números)
    const namePattern = /^\D*$/;
    if (!namePattern.test(memberData.name) || memberData.name.length > 25) {
      alert(
          "Name must not contain numbers and should be between 1 and 25 characters.");
      return;
    }

    // Validar el número de teléfono (debe ser de 10 a 12 dígitos)
    const phonePattern = /^\d{10,12}$/;
    if (!phonePattern.test(memberData.phoneNumber)) {
      alert("Phone number must be between 10 and 12 digits.");
      return;
    }

    // Validar el correo electrónico
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(memberData.email)) {
      alert("Please enter a valid email.");
      return;
    }

    // Si pasa las validaciones, enviar el formulario
    $.ajax({
      type: "POST",
      url: "/kitchensink/rest/register",
      contentType: "application/json",
      data: JSON.stringify(memberData), // Enviar los datos en formato JSON
      success: function (response) {
        // Agregar el nuevo miembro a la lista sin recargar
        const newRow = `<tr>
                    <td>${response.id}</td>
                    <td>${response.name}</td>
                    <td>${response.email}</td>
                    <td>${response.phoneNumber}</td>
                </tr>`;
        $("#membersList").append(newRow);
        $("#message").html(
            '<div class="alert alert-success">Registered!</div>');

        // Limpiar el formulario
        $("#memberForm")[0].reset();
      },
      error: function () {
        $("#message").html(
            '<div class="alert alert-danger">Error registering member.</div>');
      }
    });
  });
});
