
function initMap() { 
	var _posiAtual = posiAtual();
	var map = new google.maps.Map(document.getElementById('map'), { zoom: 17});
	var latLng = new google.maps.LatLng(_posiAtual['lat'],_posiAtual['lng']);
	var marker = new google.maps.Marker({
		position: _posiAtual,
		map: map
	});
	map.addListener('center_changed', function() {
		_lng=map.getCenter().lng();
		_lat=map.getCenter().lat();
		$('#longitude').val(_lng);
		$('#latitude').val(_lat);
		var latLng = new google.maps.LatLng(_lat,_lng);
		marker.setPosition(latLng);
	});
	map.setCenter(latLng);		
	$('#longitude,#latitude').change(function(){
		var latLng = new google.maps.LatLng($('#latitude').val(), $('#longitude').val());
		map.setCenter(latLng);
	});
};


$('#mapDistancia').ready(function(){
	var _posiAtual = posiAtual();		
	var remoto = {lat: -25.363, lng: 131.044};
	remoto['lat']=$('#mapDistancia').attr('latitude');
	remoto['lng']=$('#mapDistancia').attr('longitude');
	org=_posiAtual['lat']+','+_posiAtual['lng'];
	dst=remoto['lat']+','+remoto['lng'];
	key='AIzaSyDUsEe1_EK5qhhb_5WHkTWqnit73wX5zdY';
	markers=org+'|'+dst;
	src='https://maps.googleapis.com/maps/api/staticmap?size=400x400&markers='+markers+'&key='+key;
	$('#mapDistancia img').attr('src',src);
});



