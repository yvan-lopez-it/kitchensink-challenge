$(document).ready(function () {
  // Configura toastr
  toastr.options = {
    "closeButton": true,
    "debug": false,
    "newestOnTop": true,
    "progressBar": true,
    "positionClass": "toast-top-center",
    "preventDuplicates": true,
    "onclick": null,
    "showDuration": "300",
    "hideDuration": "1000",
    "timeOut": "5000",
    "extendedTimeOut": "1000",
    "showEasing": "swing",
    "hideEasing": "linear",
    "showMethod": "fadeIn",
    "hideMethod": "fadeOut"
  };

  $("#memberForm").submit(function (event) {
    event.preventDefault(); // Previene el comportamiento por defecto del formulario

    // Obtener los valores del formulario
    const memberData = {
      name: $("#name").val(),
      email: $("#email").val(),
      phoneNumber: $("#phoneNumber").val()
    };

    // Validar el nombre (no debe contener números y debe tener entre 1 y 25 caracteres)
    if (!validator.isAlpha(memberData.name.replace(/\s/g, ''), 'en-US')
        || !validator.isLength(memberData.name, {min: 1, max: 25})) {
      toastr.error("The name must contain only letters and should be between 1 and 25 characters.");
      return;
    }

    // Validar el número de teléfono (debe ser de 10 a 12 dígitos)
    if (!validator.isLength(memberData.phoneNumber, {min: 10, max: 12})) {
      toastr.error("Phone number must be valid and contain between 10 and 12 digits.");
      return;
    }

    // Validar el correo electrónico
    if (!validator.isEmail(memberData.email)) {
      toastr.error("Please enter a valid email address.");
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
                    <td><a href="/kitchensink/rest/members/${response.id}">/kitchensink/rest/members/${response.id}</a></td>
                </tr>`;
        $("#membersList").append(newRow);

        toastr.success("Registered successfully!");

        // Limpiar el formulario
        $("#memberForm")[0].reset();
      },
      error: function (xhr) {
        if (xhr.status === 409) {
          toastr.error('The email already exists. Please use a different email.');
        } else {
          toastr.error('Error registering member.');
        }
      }
    });
  });
});
