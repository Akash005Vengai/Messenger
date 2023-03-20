const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]{2,5}$/;
let sendotp = 0;

function sendOTP() {
	sendotp++;
	if (emailRegex.test(mail.value) && mail.value.replace(" ", "").length == mail.value.length) {
		document.getElementById("signup").style = "display:none";
		document.getElementById("signup1").style = "display:block";
		let xhr = new XMLHttpRequest();
		xhr.open('POST', 'SendOTP');
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		xhr.send("email=" + mail.value);
		if (sendotp != 1) {
			second.onclick = "";
			setTimeout(120000);
			second.onclick = "sendOTP()";
		}
	} else {
		alert("Enter valid mail");
	}
}

function init() {
	document.getElementById("signup").style = "display:block";
	document.getElementById("signup1").style = "display:none";
	mail.value = "";
	otp.value = "";
	sendotp = 0;
}

function checkOTP() {
	let xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				let json = JSON.parse(xhr.responseText);
				if (json.status == 201) {
					document.getElementById("signup1").style = "display:none";
					document.getElementById("signup2").style = "display:block";
				} else if (json.status == 200) {
					document.getElementById("signup1").style = "display:none";
					document.getElementById("signin").style = "display:block";
				} else {
					alert(json.message);
				}
			}
		}
	}
	xhr.open('POST', 'CheckOTP');
	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xhr.send("email=" + mail.value + "&otp=" + otp.value);
}

function checkSession() {
	let xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				let json = JSON.parse(xhr.responseText);
				if (json.status != 400) {
					window.location.href = "./messenger.html";
				}
			}
		}
	}
	xhr.open('GET', 'CheckSession');
	xhr.send();
}

checkSession();

function changePassword() {
	if (password3.value.trim().length < 8 || password4.value.trim().length >= 18 || password3.value.trim() != password4.value.trim()) {
		alert("Enter (8-18) character only and same password");
	} else {
		let xhr = new XMLHttpRequest();
		xhr.open('POST', 'ChangePassword');
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		xhr.onreadystatechange = function () {
			if (xhr.readyState == 4 && xhr.status == 200) {
				let json = JSON.parse(xhr.responseText);
				if (json.status == 201) {
					window.location.href = "./messenger.html"
				} else {
					alert(xhr.responseText);
				}
			}
		}
		xhr.send("email=" + mail.value + "&pass=" + password3.value);
	}
}

function signin() {
	if (password2.value.trim().length < 8 || password2.value.trim().length >= 18) {
		alert("Enter (8-18) character only !!");
	} else {
		let xhr = new XMLHttpRequest();
		xhr.onreadystatechange = function () {
			if (xhr.readyState == 4 && xhr.status == 200) {
				let json = JSON.parse(xhr.responseText);
				if (json.status == 200) {
					window.location.href = "./messenger.html"
				} else {
					alert(xhr.responseText);
				}
			}
		}
		xhr.open('POST', 'SignIn');
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		xhr.send("email=" + mail.value + "&pass=" + password2.value);
	}
}

report.onclick = function () {
	if (mail.value != 0 && emailRegex.test(mail.value)) {
		let message = prompt("Any Issue in you login ?");
		if (message.trim().length > 5) {
			let xhr = new XMLHttpRequest();
			xhr.open('POST', "ReportMessage");
			xhr.send("message=" + message + "&email=" + mail.value);
		} else {
			alert("Enter any character");
		}
	} else {
		alert("Enter any mail to send report.")
	}
}

function signup() {
	if (password.value.trim().length < 8 || password.value.trim().length >= 18 || password.value.trim() != password1.value.trim()) {
		alert("Enter (8-18) character only and same password");
	} else if (Name.value.trim().length == 0||Name.value.length>50) {
		alert("Enter name any character");
	} else if (file.value.trim().length < 10) {
		alert("Choose any image for profile")
	} else {
		let xhr = new XMLHttpRequest();
		const inputFile = document.querySelector('input[type="file"]');
		const file = inputFile.files[0];
		const reader = new FileReader();
		reader.addEventListener('load', function () {
			var obj = {};
			obj.name = Name.value.trim();
			obj.email = mail.value.toLowerCase();
			obj.pass = password.value.trim();
			obj.img = reader.result;
			xhr.open("post", "./SignUp");
			xhr.setRequestHeader("Content-Type", "application/json");
			xhr.send(JSON.stringify(obj));
		});
		reader.readAsDataURL(file);
		xhr.onreadystatechange = function () {
			if (xhr.readyState == 4) {
				if (xhr.status == 200) {
					let json = JSON.parse(xhr.responseText);
					if (json.status) {
						window.location.href = "./messenger.html";
					} else {
						alert(json.message);
					}
				}
			}
		}
	}
}

check.onclick = function () {
	if (document.getElementById("check").checked) {
		password.type = "text";
		password1.type = "text";
	} else {
		password.type = "password";
		password1.type = "password";
	}
}

check1.onclick = function () {
	if (document.getElementById("check1").checked) {
		password3.type = "text";
		password4.type = "text";
	} else {
		password3.type = "password";
		password4.type = "password";
	}
}