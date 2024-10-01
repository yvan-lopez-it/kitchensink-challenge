$(document).ready(function () {

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
    event.preventDefault();

    const memberData = {
      name: $("#name").val(),
      email: $("#email").val(),
      phoneNumber: $("#phoneNumber").val()
    };

    // Validate the name (must not contain numbers and must be between 1 and 25 characters)
    if (!validator.isAlpha(memberData.name.replace(/\s/g, ''), 'en-US')
        || !validator.isLength(memberData.name, {min: 1, max: 25})) {
      toastr.error("The name must contain only letters and should be between 1 and 25 characters.");
      return;
    }

    // Validate the phone number (must be 10 to 12 digits)
    if (!validator.isLength(memberData.phoneNumber, {min: 10, max: 12})) {
      toastr.error("Phone number must be valid and contain between 10 and 12 digits.");
      return;
    }

    // Validate email
    if (!validator.isEmail(memberData.email)) {
      toastr.error("Please enter a valid email address.");
      return;
    }

    // If validations pass, send the form
    $.ajax({
      type: "POST",
      url: "/rest/register",
      contentType: "application/json",
      data: JSON.stringify(memberData),
      success: function (response) {
        // Add the new member to the list without reloading
        const newRow =
                `<tr>
                    <td>${response.id}</td>
                    <td>${response.name}</td>
                    <td>${response.email}</td>
                    <td>${response.phoneNumber}</td>
                    <td><a href="/rest/members/${response.id}">/rest/members/${response.id}</a></td>
                </tr>`;
        $("#membersList").append(newRow);

        toastr.success("Registered successfully!");

        // Clear the form
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
