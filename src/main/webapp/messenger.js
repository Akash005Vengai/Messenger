document.getElementById("file").onchange = function () {
	let xhr = new XMLHttpRequest();
	xhr.open('POST', 'user/UpdateCurrentProfile');
	let formdata = new FormData();
	formdata.append('file', file.files[0]);
	xhr.onreadystatechange = () => {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				currentuserProfile();
			}
		}
	}
	xhr.send(formdata);
}

document.getElementById("imagevalue").onchange = () => {
	for (let i = 0; i < imagevalue.files.length; i++) {
		let xhr = new XMLHttpRequest();
		xhr.open('POST', 'user/ReceiveImage');
		let formdata = new FormData();
		formdata.append('file', imagevalue.files[i]);
		xhr.onreadystatechange = () => {
			if (xhr.readyState == 4) {
				if (xhr.status == 200) {
					let obj = {};
					obj.message = value.value;
					obj.file = xhr.responseText;
					let a = document.cookie.split("; ");
					for (var i of a) {
						if (i.split("=").length == 2 && i.split("=")[0] === "SessionID") {
							obj.cookie = i.split("=")[1];
							break;
						}
					}
					if (obj.cookie != null) {
						chatrooms.send(JSON.stringify(obj));
					} else {
						location.window.href = "./login.html";
					}
					value.value = "";
				}
			}
		}
		xhr.send(formdata);
	}
}

let rooms = {}

function checkSession() {
	let xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				let json = JSON.parse(xhr.responseText);
				if (json.status != 200) {
					window.location.href = "./login.html";
				}
			}
		}
	}
	xhr.open('GET', 'CheckSession');
	xhr.send();
}

document.addEventListener("DOMContentLoaded", function () {
	checkSession();
	currentuserProfile();
	updateSetting();
	showchats();
	showContact();
});

document.getElementById("addacontact").onclick = () =>{
	document.getElementById('showC').style='display:flex';
	Contactname.value = "" ;
	Contactmail.value = "" ;
}

document.getElementById("Contactsubmit").onclick = function () {
	if (Contactname.value != "" && Contactmail.value != "" && Contactname.value.length < 49) {
		let xhr = new XMLHttpRequest();
		xhr.onreadystatechange = function () {
			if (xhr.readyState == 4 && xhr.status == 200) {
				let json = JSON.parse(xhr.responseText);
				if (json.status) {
					document.getElementById("showC").style = 'display:none';
					let xml = new XMLHttpRequest();
					xml.open("POST", "user/SendContact");
					xml.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
					xml.send("email=" + Contactmail.value + "&name=" + Contactname.value);
					let con = document.createElement("div");
					con.setAttribute("class", "con");
					let img1 = document.createElement("img");
					setProfile(img1, Contactmail.value)
					let span1 = document.createElement("pre");
					span1.innerText = Contactname.value;
					let but = document.createElement("button");
					but.innerText = "delete";
					but.value = Contactmail.value;
					but.setAttribute("class", "delete")
					but.addEventListener("click", function () {
						let xhr = new XMLHttpRequest();
						xhr.open("get", "user/DeleteContact?user=" + this.value);
						xhr.send()
						this.parentNode.remove();
					});
					con.appendChild(img1);
					con.appendChild(span1);
					con.appendChild(but);
					con.addEventListener("click", function () {
						let xhr = new XMLHttpRequest()
						let id = this.childNodes[2].value;
						xhr.open("get", 'user/getChatID?user=' + id)
						xhr.send();
						xhr.onreadystatechange = () => {
							if (xhr.status == 200 && xhr.readyState == 4) {
								showMessage("c" + xhr.responseText, id);
							}
						}
					})
					contact1.appendChild(con);
				} else {
					alert("enter valid gmail and not already saved mail")
				}
			}
		}
		xhr.open("GET", "user/CheckUser?user=" + Contactmail.value);
		xhr.send()
	} else {
		alert("Please fill all field")
	}
}

function logout() {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', 'user/LOGOUT');
	xhr.send();
}

function currentuserProfile() {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', 'user/currentProfile');
	xhr.responseType = 'blob';
	xhr.onload = function () {
		let imageUrl = URL.createObjectURL(this.response);
		profile.src = imageUrl;
		Profile.src = imageUrl;
	};
	xhr.send();
}

function showContact() {
	let xml = new XMLHttpRequest();
	xml.open('GET', 'user/SendContact');
	xml.onreadystatechange = function () {
		if (xml.readyState == 4 && xml.status == 200) {
			const json = JSON.parse(xml.responseText);
			let sortable = [];
			contact1.innerText = "";
			for (const vehicle in json) {
				sortable.push([vehicle, json[vehicle]]);
			}
			sortable.sort(function (a, b) {
				return a[1] - b[1];
			});
			for (let i = 0; i < sortable.length; i++) {
				let con = document.createElement("div");
				con.setAttribute("class", "con");
				let img1 = document.createElement("img");
				setProfile(img1, sortable[i][0])
				let span1 = document.createElement("pre");
				span1.innerText = sortable[i][1] 
				let but = document.createElement("button");
				but.innerText = "delete";
				but.value = sortable[i][0];
				but.setAttribute("class", "delete")
				but.addEventListener("click", function () {
					let xhr = new XMLHttpRequest();
					xhr.open("get", "user/DeleteContact?user=" + this.value);
					xhr.send()
					this.parentNode.remove();
				})
				con.appendChild(img1);
				con.appendChild(span1);
				con.appendChild(but);
				con.addEventListener("click", function () {
					let xhr = new XMLHttpRequest()
					let id = this.childNodes[2].value;
					xhr.open("get", 'user/getChatID?user=' + id)
					xhr.send();
					xhr.onreadystatechange = () => {
						if (xhr.status == 200 && xhr.readyState == 4) {
							showMessage("c" + xhr.responseText, id);
						}
					}
				})
				contact1.appendChild(con);
			}
		}
	}
	xml.send();
}

function showGroups() {
	let xhr = new XMLHttpRequest();
	xhr.open("get", "user/SendGroupID");
	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				let json = JSON.parse(xhr.responseText);
				if(Object.keys(json).length!=details2.childNodes.length){
				details2.innerHTML = "";
				for (const value of Object.keys(json)) {
					let div = document.createElement("div");
					div.setAttribute("class", "groups");
					div.setAttribute("id", "g" + value);
					start("g" + value);
					let img = document.createElement("img");
					setgroupProfile(img, value);
					let pre = document.createElement("pre");
					pre.innerText = json[value].split(" ")[0];
					let span = document.createElement("span");
					let xhr = new XMLHttpRequest();
					xhr.open("get", "user/notviewmessageforgroup?id=" + value);
					xhr.send();
					xhr.onreadystatechange = () => {
						if (xhr.readyState == 4 && xhr.status == 200) {
							if (xhr.responseText !== "0") {
								span.innerText = xhr.responseText;
							}
						}
					}
					div.appendChild(img); blob:http://localhost:8080/fa0d01a6-e140-444c-b72e-e3efdffbbc8f
					div.appendChild(pre);
					div.appendChild(span);
					div.addEventListener("click", function () {
						if (this.childNodes[2].innerText != "") {
							seenallGroupmessage(this.id.substring(1));
						}
						this.childNodes[2].innerText = "";
						showGroupMessage(this.id, this.childNodes[1].innerText);
					})
					details2.appendChild(div);
					div.style = "order:" + json[value].split(" ")[1];
				}
				start(0);
			}
			}
		}
	}
	xhr.send()
}

function setProfile(element, user) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "user/sendProfile?name=" + user);
	xhr.responseType = 'blob';
	xhr.onload = function () {
		let imageUrl = URL.createObjectURL(this.response);
		element.src = imageUrl;
	};
	xhr.send();
}

function setgroupProfile(element,user) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "user/sendGroupProfile?id="+user);
	xhr.responseType = 'blob';
	xhr.onload = function () {
		let imageUrl = URL.createObjectURL(this.response);
		element.src = imageUrl;
	};
	xhr.send();
}

function updateSetting() {
	let xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == 200) {
			let json = JSON.parse(xhr.responseText);
			if (json.status == 200) {
				let selectElement = document.getElementById("showProfile");
				for (let i = 0; i < selectElement.options.length; i++) {
					const option = selectElement.options[i];
					if (option.value === json.showProfile) {
						option.selected = true;
					}
				}
				sendTextColor.value = json.sendColor;
				document.querySelector(':root').style.setProperty('--send', json.sendColor);
				receiveTextColor.value = json.receiveColor;
				document.querySelector(':root').style.setProperty('--receive', json.receiveColor);
				sendNameColor.value = json.sendNameColor;
				document.querySelector(':root').style.setProperty('--send1', json.sendNameColor);
				receiveNameColor.value = json.receiveNameColor;
				document.querySelector(':root').style.setProperty('--receive1', json.receiveNameColor);
			}
		}
	}
	xhr.open('GET', "user/SettingDetails");
	xhr.send();
}

function showMessage(id, name) {
	table.innerHTML = "";
	let xhr = new XMLHttpRequest();
	xhr.open("get", "user/SendChatMessage?id=" + id.substring(1))
	xhr.send();
	xhr.onreadystatechange = () => {
		if (xhr.readyState == 4 && xhr.status == 200) {
			let json = JSON.parse(xhr.responseText);
			if (json.arr != null) {
				userDetails.style = "display:flex";
				value.style = "display:block";
				value.value = "";
				send_file.style = "display:block";
				userDetails.innerHTML = "";
				let img = document.createElement("img");
				setProfile(img, name);
				let userspan = document.createElement("span");
				userspan.innerText = name;
				let lordicon = document.createElement("lord-icon");
				lordicon.setAttribute("src", "https://cdn.lordicon.com/nhfyhmlt.json");
				lordicon.setAttribute("trigger", "click");
				lordicon.setAttribute("colors", "primary:#ffffff");
				userDetails.appendChild(img);
				userDetails.appendChild(userspan);
				userDetails.appendChild(lordicon);
				lordicon.addEventListener("click", function () {
					userDetails.innerHTML = "";
					table.innerHTML = "";
					start(0);
					value.style = "display:none";
					send_file.style = "display:none";
					userDetails.style = "display:none"
				})
				let arr = JSON.parse(json.arr);
				start(id);
				for (var i of arr) {
					let message1 = JSON.parse(i);
					putChat(message1);
				}
			} else {
				alert("You don't have access to this");
			}

		}
	}
}

function showGroupMessage(id,name){
	table.innerHTML = "";
	let xhr = new XMLHttpRequest();
	xhr.open("get", "user/SendGroupMessage?id="+id.substring(1))
	xhr.send();
	xhr.onreadystatechange = () => {
		if (xhr.readyState == 4 && xhr.status == 200) {
			let json = JSON.parse(xhr.responseText);
			if (json.arr != null) {
				userDetails.style = "display:flex";
				value.style = "display:block";
				value.value = "";
				send_file.style = "display:block";
				userDetails.innerHTML = "";
				let img = document.createElement("img");
				setgroupProfile(img, id.substring(1));
				let userspan = document.createElement("span");
				userspan.innerText = name;
				let lordicon = document.createElement("lord-icon");
				lordicon.setAttribute("src", "https://cdn.lordicon.com/nhfyhmlt.json");
				lordicon.setAttribute("trigger", "click");
				lordicon.setAttribute("colors", "primary:#ffffff");
				userDetails.appendChild(img);
				userDetails.appendChild(userspan);
				userDetails.appendChild(lordicon);
				lordicon.addEventListener("click", function () {
					userDetails.innerHTML = "";
					table.innerHTML = "";
					start(0);
					value.style = "display:none";
					send_file.style = "display:none";
					userDetails.style = "display:none"
				})
				let arr = JSON.parse(json.arr);
				start(id);
				for (var i of arr) {
					let message1 = JSON.parse(i);
					putGroupChat(message1);
				}
			} else {
				alert("You don't have access to this");
			}

		}
	}
}

function showchats() {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', 'user/SendChatId');
	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				let json = JSON.parse(xhr.responseText);
				if(Object.keys(json).length!=details1.childNodes.length){
					for (const value of Object.keys(json)) {
						let div = document.createElement("div");
						div.setAttribute("class", "chats");
						div.setAttribute("id", "c" + value)
						start("c" + value);
						let img = document.createElement("img");
						setProfile(img, json[value].split(" ")[0]);
						let pre = document.createElement("pre");
						pre.innerText = json[value].split(" ")[0];
						let span = document.createElement("span");
						let xhr = new XMLHttpRequest();
						xhr.open("get", "user/notviewmessagecount?id=" + value);
						xhr.send();
						xhr.onreadystatechange = () => {
							if (xhr.readyState == 4 && xhr.status == 200) {
								if (xhr.responseText !== "0") {
									span.innerText = xhr.responseText;
								}
							}
						}
						div.appendChild(img);
						div.appendChild(pre);
						div.appendChild(span);
						div.addEventListener("click", function () {
							if (this.childNodes[2].innerText != "") {
								seenallmessage(this.id.substring(1));
							}
							this.childNodes[2].innerText = "";
							showMessage(this.id, this.childNodes[1].innerText);
						})
						details1.appendChild(div);
						div.style = "order:" + json[value].split(" ")[1];
					}
					start(0);
			}
			}
		}
	}
	xhr.send();
}

document.getElementById("Profile").onclick = () => file.click();

function sender(key, value) {
	let xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == 200) {
			let json = JSON.parse(xhr.responseText);
			if (json.status == 1000) {
				window.location.href = "./login.html";
			} else if (json.status == 200) {
				updateSetting();
			} else {
				alert(json.message);
			}
		}
	}
	xhr.open('POST', 'user/UpdateProfile');
	xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhr.send("key=" + key + "&value=" + value);
}

function seenallmessage(id) {
	let xhr = new XMLHttpRequest();
	xhr.open("get", "user/readAllMessage?id=" + id);
	xhr.send();
}
function seenallGroupmessage(id) {
	let xhr = new XMLHttpRequest();
	xhr.open("get", "user/ReadAllGroupMessage?id=" + id);
	xhr.send();
}

document.getElementById("value").addEventListener("keydown", (event) => {
	if (event.key == "Enter") {
		let value = this.value.value.trim();
		if (value.length == 0) {
			alert("Type any character")
		} else {
			let obj = {};
			obj.message = value;
			obj.file = 0;
			let a = document.cookie.split("; ");
			for (var i of a) {
				if (i.split("=").length == 2 && i.split("=")[0] === "SessionID") {
					obj.cookie = i.split("=")[1];
					break;
				}
			}
			if (obj.cookie != null) {
				chatrooms.send(JSON.stringify(obj));
			} else {
				location.window.href = "./messenger.html";
			}
			this.value.value = "";
		}
	}
});

let chatrooms;

function start(value) {
	if (rooms[value] != null) {
		chatrooms = rooms[value];
	} else {
		chatrooms = new WebSocket("ws://" + window.location.hostname + ":8080/Messenger/chatroom/" + value);
		rooms[value] = chatrooms;
	}

	chatrooms.onmessage = function (message) {
		let message1 = JSON.parse(message.data);
		if(message1.id[0]=="c"){
			let chat = document.getElementsByClassName("chats");
			for (let i = 0; i < chat.length; i++)
				chat[i].style.order = +(chat[i].style.order) + 1
			if (document.getElementById(message1.id)) {
				document.getElementById(message1.id).style = "order:" + 0;
				if (message1.sender != "You") {
					if (document.getElementById(message1.id).childNodes[2].innerText == "") {
						document.getElementById(message1.id).childNodes[2].innerText = 1;
					} else {
						document.getElementById(message1.id).childNodes[2].innerText = (+(document.getElementById(message1.id).childNodes[2].innerText)) + 1;
					}
				}
			}
			if (message.currentTarget.url == chatrooms.url) {
				putChat(message1);
			}
		}else if(message1.id[0]=="g"){
			let chat = document.getElementsByClassName("groups");
			for (let i = 0; i < chat.length; i++)
				chat[i].style.order = +(chat[i].style.order) + 1
			if (document.getElementById(message1.id)) {
				document.getElementById(message1.id).style = "order:" + 0;
				if (message1.sender != "You") {
					if (document.getElementById(message1.id).childNodes[2].innerText == "") {
						document.getElementById(message1.id).childNodes[2].innerText = 1;
					} else {
						document.getElementById(message1.id).childNodes[2].innerText = (+(document.getElementById(message1.id).childNodes[2].innerText)) + 1;
					}
				}
			}
			if (message.currentTarget.url == chatrooms.url) {
				putGroupChat(message1);
			}
		}
	};
}

function putChat(message1) {
	let div = document.createElement("div");
	div.setAttribute("class", message1.sender == "You" ? "send" : "receive");
	let pre = document.createElement("pre");
	pre.innerText = message1.message;
	let span = document.createElement("span");
	span.innerText = message1.time;
	span.setAttribute("class", message1.sender == "You" ? "sendspan" : "receivespan")
	if (message1.sender == "You") {
		if (message1.file != 0) {
			let xhr = new XMLHttpRequest();
			xhr.open("get","user/ReceiveImage?id="+message1.file);
			xhr.send();
			xhr.responseType = "blob"
			xhr.onreadystatechange = ()=>{
				if(xhr.status==200&&xhr.readyState==4){
					// console.log(xhr.getResponseHeader('content-type'))
				}
			}
			let a = document.createElement("a");
			a.setAttribute("href", "user/ReceiveImage?id=" + message1.file);
			a.innerText = "download file";
			div.appendChild(a);
		}
		div.appendChild(pre);
		div.appendChild(span);
	} else {
		div.appendChild(span);
		if (message1.file != 0) {
			let a = document.createElement("a");
			a.setAttribute("href", "user/ReceiveImage?id=" + message1.file);
			a.innerText = "download file";
			div.appendChild(a);
		}
		div.appendChild(pre);
	}
	table.appendChild(div);
	document.getElementById("table").scrollTop = document.getElementById("table").scrollHeight;
}

function putGroupChat(message1) {
	let div = document.createElement("div");
	div.setAttribute("class", message1.sender == "You" ? "send" : "receive");
	let pre = document.createElement("pre");
	pre.innerText = message1.message;
	let span = document.createElement("span");
	span.innerText = message1.time;
	span.setAttribute("class", message1.sender == "You" ? "sendspan" : "receivespan")
	if (message1.sender == "You") {
		if (message1.file != 0) {
			let xhr = new XMLHttpRequest();
			xhr.open("get","user/ReceiveImage?id="+message1.file);
			xhr.send();
			xhr.responseType = "blob"
			xhr.onreadystatechange = ()=>{
				if(xhr.status==200&&xhr.readyState==4){
					console.log(xhr.getResponseHeader('content-type'))
				}
			}
			let a = document.createElement("a");
			a.setAttribute("href", "user/ReceiveImage?id=" + message1.file);
			a.innerText = "download file";
			div.appendChild(a);
		}
		div.appendChild(pre);
		div.appendChild(span);
	} else {
		let div1 = document.createElement("div");
		let img1 = document.createElement("img");
		setProfile(img1,message1.sender);
		let span1 = document.createElement("span");
		span1.innerText = message1.sender;
		div1.appendChild(img1);
		div1.appendChild(span1)
		div.appendChild(div1);
		div.appendChild(span);
		if (message1.file != 0) {
			let a = document.createElement("a");
			a.setAttribute("href", "user/ReceiveImage?id=" + message1.file);
			a.innerText = "download file";
			div.appendChild(a);
		}
		div.appendChild(pre);
	}
	table.appendChild(div);
	document.getElementById("table").scrollTop = document.getElementById("table").scrollHeight;
}

document.getElementById("createGroup").onclick = () => {
	createGroupDetails.style = "display:flex";
	createGroupDetails.childNodes[1].style = "display:block";
	createGroupDetails.childNodes[3].style = "display:block";
	members = [];
	let xml = new XMLHttpRequest();
	xml.open('GET', 'user/SendContact');
	xml.onreadystatechange = function () {
		if (xml.readyState == 4 && xml.status == 200) {
			const json = JSON.parse(xml.responseText);
			let sortable = [];
			contactAll.innerText = "";
			peoples.innerHTML = "";
			for (const vehicle in json) {
				sortable.push([vehicle, json[vehicle]]);
			}
			sortable.sort(function (a, b) {
				return a[1] - b[1];
			});
			for (let i = 0; i < sortable.length; i++) {
				putContact(sortable[i]);
			}
		}
	}
	xml.send();
}

let members = [];

function putContact(sortable) {
	let con = document.createElement("div");
	con.setAttribute("class", "con");
	let img1 = document.createElement("img");
	setProfile(img1, sortable[0])
	let span1 = document.createElement("pre");
	span1.innerText = sortable[1];
	con.appendChild(img1);
	con.appendChild(span1);
	con.addEventListener("click",()=>{ 
		con.remove();
		members.push(sortable[0])
		if(members.length!=0){
			confirm1.style = 'display:block';
		}
		let div = document.createElement("div");
		let img = document.createElement("img");
		setProfile(img,sortable[0]);
		let span = document.createElement("span");
		span.innerText = sortable[1];
		div.appendChild(img);
		div.appendChild(span)
		div.addEventListener("click",()=>{
			members.splice(members.indexOf(sortable[0]),1);
			if(members.length==0){
				confirm1.style = "display:none";
			}
			div.remove();
			putContact(sortable);
		})
		peoples.appendChild(div)
	});
	contactAll.appendChild(con);
}

confirm1.onclick = () =>{
	groupDes.value = "";
	groupName.value = "";
	confirm1.style = "display:none";
	create.style = "display:none";
	img.src = "user/sendGroupProfile?id=0";
	groupDetails.style = "display:flex";
}

function hide(){
	let div = document.getElementById('createGroupDetails');
	div.style='display:none';
	for(let i=0;i<div.childNodes.length;i++)
		div.childNodes[i].style = "display:none";
}

function creategroup() {
	if(groupName.value.trim()!=""&&groupDes.value.trim()!=""&&members.length!=0){
		let xhr = new XMLHttpRequest();
		xhr.open("post","user/createGroup");
		let formdata = new FormData();
		formdata.append("members",JSON.stringify(members));
		formdata.append("name",groupName.value.trim());
		formdata.append("des",groupDes.value.trim());
		if('http://localhost:8080/Messenger/user/sendGroupProfile?id=0'!=img.src){
			formdata.append("img",groupImage.files[0]);
			formdata.append("image",true);
		}else{
			formdata.append("image",false);
		}
		xhr.onreadystatechange = () =>{
			if(xhr.status==200&xhr.readyState==4){
				hide();
				showGroups();
			}
		}
		xhr.send(formdata);
	}else{
		alert("Fill all field");
	}
}

document.getElementById("groupImage").addEventListener("change", function(){
   const reader = new FileReader();
   reader.addEventListener("load", ()=>{
        img.src = reader.result;
   });
   reader.readAsDataURL(this.files[0]);
 });