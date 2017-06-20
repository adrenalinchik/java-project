var activeOwners = [];

function makeElementInvisible(element) {
    document.getElementById(element).style.display = 'none';
}

function makeElementVisible(element) {
    document.getElementById(element).style.display = 'block';
}
function makeAddButtonsInvisible() {
    makeElementInvisible('add_owner');
    makeElementInvisible('add_vehicle');
    makeElementInvisible('add_reservation');
    makeElementInvisible('add_parking');
}

function addOwnerButtonVisible() {
    makeElementInvisible('add_vehicle');
    makeElementInvisible('add_reservation');
    makeElementInvisible('add_parking');
    makeElementVisible('add_owner');
}

function addVehicleButtonVisible() {
    makeElementInvisible('add_owner');
    makeElementInvisible('add_reservation');
    makeElementInvisible('add_parking');
    makeElementVisible('add_vehicle');
}

function makeReservationButtonVisible() {
    makeElementInvisible('add_owner');
    makeElementInvisible('add_vehicle');
    makeElementInvisible('add_parking');
    makeElementVisible('add_reservation');
}

function addParkingButtonVisible() {
    makeElementInvisible('add_owner');
    makeElementInvisible('add_reservation');
    makeElementInvisible('add_vehicle');
    makeElementVisible('add_parking');
}
function addActivityRowDashboard(objectType, id, action) {
    let date = new Date();
    let dd = date.getDate();
    let MM = date.getMonth() + 1;
    let yyyy = date.getFullYear();
    let hh = date.getHours();
    let mm = date.getMinutes();
    let sec = date.getSeconds();
    let markup = "<tr><td>" + objectType + "</td><td>" + id + "</td><td>" + action + "</td><td>" + yyyy + "-" + MM + "-" + dd + " " + hh + ":" + mm + ":" + sec + "</td></tr>";
    $("#dashboardActivityTable").append(markup);
    switch (action) {
        case "created":
            $('#dashboardActivityTable tr:last').css("background", "hsla(120, 60%, 70%, 0.3)");
            break;
        case "updated":
            $('#dashboardActivityTable tr:last').css("background", "hsla(290, 60%, 70%, 0.3)");
            break;
        case "deleted":
            $('#dashboardActivityTable tr:last').css("background", "hsla(356, 90%, 50%, 0.3)");
            break;
    }
    let activity = {
        objectType: objectType.toUpperCase(),
        objectId: id,
        actionType: action.toUpperCase(),
        //createdDate: new Date()
    };
    $.ajax({
        headers: {
            'Content-Type': 'application/json'
        },
        url: '/parking/api/activity/create',
        type: 'post',
        data: JSON.stringify(activity)
    })
}

function getOwnersToDataList(dataList) {
    $('#reservOwnerList').empty();
    var ownerList = document.getElementById(dataList);
    $.ajax({
        url: '/parking/api/owners/active',
    }).done(function (owners) {
        activeOwners = owners;
        owners.map(function (owner) {
            var option = document.createElement('option');
            option.value = owner.firstName + " " + owner.lastName;
            ownerList.appendChild(option);
        })
    });
}

function getOwnerId(ownerInput) {
    var id;
    var ownerName = ownerInput.split(" ");
    for (var i = 0; i < activeOwners.length; i++) {
        var first = activeOwners[i].firstName;
        var last = activeOwners[i].lastName;
        if (first === ownerName[0] && last === ownerName[1]) {
            id = activeOwners[i].id;
        }
    }
    return id;
}

function closeOpenModal(modalLocator) {
    $(modalLocator).modal('toggle');
}
$(document).ready(function () {
    $('#dashboardTab').on('click', function () {
        makeAddButtonsInvisible();
    });
    $('#ownerTab').on('click', function () {
        addOwnerButtonVisible();
    });
    $('#vehicleTab').on('click', function () {
        addVehicleButtonVisible();
    });
    $('#reservationTab').on('click', function () {
        makeReservationButtonVisible();
    });
    $('#parkingTab').on('click', function () {
        addParkingButtonVisible();
    })
});
