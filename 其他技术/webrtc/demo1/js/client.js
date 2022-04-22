'use strict';

//set constraints
const mediaStreamConstraints = {
    video: true
};

//set up to exchange only video.
const offerOptions = {
    offerToReceiveVideo: 1,
};

//Define peer connections, streams and video elements.
const localVideo = document.getElementById('localVideo');
const remoteVideo = document.getElementById('remoteVideo');

let localStream;
let remoteStream;

//Define action buttons.
const startButton = document.getElementById("startButton");
const callButton = document.getElementById('callButton');
const hangupButton = document.getElementById('hangupButton');

//Sets the MediaStream as the video element src.
// 如果 getUserMedia 获得流，则会回调该函数
// 在该函数中一方面要将获取的音视频流展示出来
// 另一方面是保存到 localSteam
function gotLocalMediaStream(mediaStream) {
    localVideo.srcObject = mediaStream;
    localStream = mediaStream;
    trace("Received local stream.")
    callButton.disabled = false;
}

//Handles error by logging a message to the console.
function handleLocalMediaStreamError(error) {
    trace(`navigator.getUserMedia error: ${error.toString()}`);
}

//Handles remote MediaStream success by adding it as the remoteVideo src.
function gotRemoteMediaStream(event) {
    const mediaStream = event.stream;
    remoteVideo.srcObject = mediaStream;
    remoteStream = mediaStream;
    trace('Remote peer connection received remote stream.')
}

// Connects with new peer candidate.
function handleConnection(event) {
    const peerConnection = event.target;
    const iceCandidate = event.candidate;

    if(iceCandidate) {
        const newIceCandidate = new RTCIceCandidate(iceCandidate);
        const otherPeer = getOtherPeer(peerConnection);

        otherPeer.addIceCandidate(newIceCandidate)
            .then(() => {
                handleConnectionSuccess(peerConnection);
            }).catch((error) => {
                handleConnectionFailure(peerConnection,error);
            })
        
        trace(`${getPeerName(peerConnection)} ICE candidate:\n` + 
                `${event.candidate.candidate}.`)
    }
}

// Logs that the connection successded.
function handleConnectionSuccess(peerConnection) {
    trace(`${getPeerName(peerConnection)} addIceCandidate success.`);
}

// Logs that the connection failed.
function handleConnectionFailure(peerConnection, error) {
    trace(`${getPeerName(peerConnection)} failed the add ICE Candidate:\n` + 
        `${error.toString()}.`)
}

// Logs changes to the connection state.
function handleConnectionChange(event) {
    const peerConnection = event.target;
    console.log('ICE state change event: ',event);

    trace(`${getPeerName(peerConnection)} ICE state: ` +
    `${peerConnection.iceConnectionState}.`);
}

// Logs error when setting session description fails.
function setSessionDescriptionError(error) {
    trace(`Failed to create session description: ${error.toString()}.`);
}

// Logs success when setting session description.
function setDescriptionSuccess(peerConnection, functionName) {
    const peerName = getPeerName(peerConnection);
    trace(`${peerName} ${functionName} complete.`);
}

// Logs success when localDescription is set.
function setLocalDescriptionSuccess(peerConnection) {
    setDescriptionSuccess(peerConnection,'setLocalDescription');
}

function setRemoteDescriptionSuccess(peerConnection) {
    setDescriptionSuccess(peerConnection,'setRemoteDescription')
}

function createdOffer(description) {
    trace(`Offer from localPeerConnection:\n${description.sdp}`);

    trace('localPeerConnection setLocalDescription start.');
    localPeerConnection.setLocaldescription(description)
        .then(() => {
            setLocalDescriptionSuccess(localPeerConnection);
        }).catch(setSessionDescriptionError);
    
    trace('remotePeerConnection setRemoteDescription start.');
    remotePeerConnection.setRemoteDescription(description)
        .then(() => {
            setRemoteDescriptionSuccess(remotePeerConnection);
        }).catch(setSessionDescriptionError);
    
    trace('remotePeerConnection createAnswer start.');
    remotePeerConnection.createAnswer()
        .then(createdAnswer)
        .catch(setSessionDescriptionError)
}

function createdAnswer(description) {
    trace(`Answer from remotePeerConnection:\n${description.sdp}.`);

    trace('remotePeerConnection setLocalDescription start.');
    remotePeerConnection.setLocaldescription(description)
        .then(() => {
            setLocalDescriptionSuccess(remotePeerConnection)
        }).catch(setSessionDescriptionError)
    
    trace('localPeerConnection setRemoteDescription start.');
    localPeerConnection.setRemoteDescription(description)
        .then(() => {
            setRemoteDescriptionSuccess(localPeerConnection)
        }).catch(setSessionDescriptionError)
}


callButton.disabled = true;
hangupButton.disabled = true;

function startAction() {
    startButton.disabled = true;
    navigator.mediaDevices.getUserMedia(mediaStreamConstraints)
        .then(gotLocalMediaStream).catch(handleLocalMediaStreamError)
    trace('Requesting local stream.');
}

function callAction() {
    callButton.disabled = true;
    hangupButton.disabled = false;

    trace("Starting call.");

    const videoTracks = localStream.getVideoTracks();
    const audioTracks = localStream.getAudioTracks();
    if(videoTracks.length > 0) {
        trace(`Using video device: ${videoTracks[0].label}.`);
    }

    if(audioTracks.length > 0) {
        trace(`Using audio device: ${audioTracks[0].label}.`);
    }

    const servers = null; // Allows for RTC server configuration.

    localPeerConnection = new RTCPeerConnection(servers);
    trace('Created local peer connection object localPeerConnection.');

    localPeerConnection.addEventListener('iceandidate',handleConnection);
    localPeerConnection.addEventListener('iceconnectionstatechange',handleConnectionChange);

    remotePeerConnection = new RTCPeerConnection(servers);
    trace('Created remote peer connection object remotePeerConnection.');

    remotePeerConnection.addEventListener('icecandidate',handleConnection);
    remotePeerConnection.addEventListener('iceconnectionstatechange',handleConnectionChange);
    remotePeerConnection.addEventListener('addstream',gotRemoteMediaStream);

    localPeerConnection.addStream(localStream);
    trace('Added local stream to localPeerConnection.');

    trace('localPeerConnection createOffer start.');
    localPeerConnection.createOffer(offerOptions)
        .then(createdOffer).catch(setSessionDescriptionError);
}

function hangupAction() {
    localPeerConnection.close();
    remotePeerConnection.close();
    localPeerConnection = null;
    remotePeerConnection = null;
    hangupButton.disabled = true;
    callButton.disabled = false;
    trace('Ending call.')
}

startButton.addEventListener('click',startAction);
callButton.addEventListener('click',callAction);
hangupButton.addEventListener('click',hangupAction);

function getOtherPeer(peerConnection) {
    return (peerConnection === localPeerConnection) ? remotePeerConnection : localPeerConnection;
}

function getPeerName(peerConnection) {
    return (peerConnection === localPeerConnection) ?
      'localPeerConnection' : 'remotePeerConnection';
}


function trace(text) {
    text = text.trim();
    const now = (window.performance.now / 1000).toFixed(3);

    console.log(now,text);
}