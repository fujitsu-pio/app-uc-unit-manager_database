/**
 * Personium
 * Copyright 2016 FUJITSU LIMITED
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Global variable
var width			= 1,//1210,
	height			= 1,//540,
	ufRadius		= 5,
	charge			= -400,
	gravity			= .6,
	HIDE_POSITION	= -100,
	SHOW_CELLNAME	= 20,
	MAX_NODE_COUNT	= 101,
	MAX_NODE_LENGTH	= 1000,
	IMG_WIDTH		= 150,
	IMG_HEIGHT		= 150,

	fNodeR			= 1,
	fNodeX			= 1,
	fNodeY			= 1,
	selectedNodeID	= -1,
	selectedCellName= "",
	selectedNode	= "",
	token			= "",
	cellList		= "",
	jsonString		= "",
	lenJSONstring	= "",
	response		= 0,
	booldblclick	= 0,
	nodes			= [],
	Extnodes		= [],
	extNodesArray	= [],
	links			= [],
	fNodePos		= [],
	clicks			= 0,
	MSG001			= "External cell can't be accessed",
	MSG002			= "",
	fNodestorage	= [];

	persistTokenForGraph();

	token			= getClientStore().token;
	cellList		= getCellList();
	jsonString		= JSON.parse(cellList);
	lenJSONstring	= jsonString.length;
	
	

	getArrayNodes();
	getArrayOfExtNodes();
	getArrayOfLinks();
	
	if (lenJSONstring > MAX_NODE_LENGTH) {
		document.getElementById("dvemptyTableMessage").style.display = "block";
		return;
	}

	var svg = d3
					.select("#GraphContainer")
					.append("svg")
					.attr("class", "svg")
					.attr("width", width)
					.attr("height", height);

	setSVGDimension();

	setFNodeCenterAndRadius();

	ufRadius = getNodeRadius(width, height, 10, lenJSONstring);

	

	var xcolor = d3
					.scale.ordinal()
					//.range([ "3E89E6", "GREEN", "BLUE","GREY"])
					//.range([ "RED", "BLUE","GREY", "YELLOW"])//do not change the sequence.
					.range([ "04B404", "FF0040","A4A4A4", "FACC2E"])//By Azeem & Imran san
					.domain([ 0, 1, 2, 3 ]);


	
	//This div add as a tooltip
	var tooltipdiv = d3
					.select("body")
					.append("div")
					.attr("class", "tooltip");

	var force = d3.layout.force()
					.size([ width, height])
					.nodes(nodes)
					.links(links);

	createMarker();

	unfocusForceView();

	svgResize();
	
	var link = svg
					.selectAll(".link")
					.data(links)
					.enter()
					.append("line")
					.attr("class", "link");

	link
					.filter(function(d) {
						return d.bond > 1;
					})
					.append("line")
					.attr("class", "separator");

	var node = svg
					.selectAll(".node")
					.data(nodes)
					.enter()
					.append("circle")
					.attr("class", "node")
					.call(force.drag);

	node
					.attr("name", function(d) { return d.name;})
					.attr("createdDate", function(d) { return d.cellCreatedDate;})
					.attr("color", function(d) { return d.color;})
					.attr("ID", function(d) { return d.ID;})
					.attr("weight", function(d) { return d.weight;})
					.attr("DisplayInfo", function(d) { return d.name + "\n" + d.cellCreatedDate;})
					.attr("r", function(d) { return ufRadius;})
					.style("fill", function(d) { return xcolor(d.color);})
					.on("dblclick",clickWindow)
					.on("click",clickWindow)
					.on("mouseover", mouseOver)
					.on("mouseout", mouseOut);

	//Add the SVG Text Element to the svgContainer to show the CellName
	var text = svg
					.selectAll("text")
					.data(nodes)
					.enter()
					.append("text");

	var cellName = text
					.attr("x", function(d) { return d.x; })
					.attr("y", function(d) { return d.y; })
					.attr("font-family", "sans-serif")
					.attr("font-size", "10px")
					.attr("text-anchor", "middle");

	var unfocusViewImage = svg
					.append("image")
					.attr("width", 30)
					.attr("height", 30)
					.attr("id", "unfocusView")
					.attr("class", "backBtn")
					.style("cursor", "pointer")
					.style("visibility", "hidden")
					.attr("xlink:href", "..\\newImages\\backIconSocialGraph.png")
					.attr("x",2)
					.attr("y",480)
					.on("click", movePrevious)
					.on("mouseover",changeUnfocusViewPic)
					.on("mouseout",resetUnfocusViewPic);

	var cellProfileImage = svg
					.append("image")
					.attr("width", IMG_WIDTH)
					.attr("height", IMG_HEIGHT)
					.attr("x", fNodeX - fNodeR * 0.35)
					.attr("y", fNodeY - fNodeR * 0.90)
					.style("visibility", "hidden");
	
	var cellProfileDesc = svg
					.append("foreignObject")
					.attr("width", 250)
					.attr("height", 300)
					.attr("x", fNodeX - fNodeR * 0.35)
					.attr("y", fNodeY - fNodeR * 0.90 + IMG_HEIGHT)
					.attr("id", "fo1")
					.append("xhtml:body")
					.style("font", "10px 'Arial'")
					.style("background","transparent")
					.style("visibility", "hidden");

	resetNodeSize();

	displayCellName();

function getNodeRadius(pWidth, pHeight, pFactor, numCells) {
	if(numCells == 0) {
		numCells = 1;
	}

	if(pFactor == 0) {
		pFactor = 1;
	}
	return Math.floor(Math.sqrt((pWidth * pHeight) / (pFactor * numCells)));
}
	

function persistTokenForGraph () {
	var jsonData = {};
	var tempToken				= sessionStorage.tempToken;
	var envName					= sessionStorage.selectedEnvName;
	var unitURL					= sessionStorage.selectedUnitUrl;
	jsonData["token"]			= tempToken;
	jsonData["environmentName"]	= envName;
	jsonData["baseURL"]			= unitURL;
	setClientStore(jsonData);
}

function unfocusForceView() {
	if (lenJSONstring > 50) {
		force
			.linkDistance(10 * ufRadius)
			.on("tick", unfocusTick)
			.start();
	}
	else {
		force
			.charge(-300) //-30
			.gravity(.01)//0.1 default
			.friction(.9)
			.linkDistance(5 * ufRadius)
			.on("tick", unfocusTick)
			.start();
	}

}

function unfocusTick() {
	ufLinkTick();

	cellNameTick();

	ufNodeTick();

}

function displayCellName() {
	cellName
		.text(function(d) {
			var cellName;
			cellName =d.name;
			if(d.name.length > 10) {
				cellName = d.name.substring(0, 7)+ "...";
			}
			return cellName;
		});
}

function displayRelationName() {
	link
		.selectAll("text")
		.attr("x", function(d) { return (d.source.x + d.target.x) / 2;})
		.attr("y", function(d) { return (d.source.y + d.target.y) / 2;})
		.attr("rotate", 0)
		.attr("dx", 5)
		.attr("dy", 0);
}

function showToolTip(NodeObject) {
	
	//var ypos = Number(NodeObject.attr("cy"));
	//var xpos = Number(NodeObject.attr("cx")) + 1.10*Number(NodeObject.attr("r"));

	var ypos = Number(d3.event.pageY);
	var xpos = Number(d3.event.pageX) + 10;
	
	tooltipdiv
		.style("visibility", "visible")
		.style("opacity", 0.9)
		.style("left", xpos + "px")
		.style("top", ypos + "px")
		.text(NodeObject.attr("DisplayInfo"));

}

function hideToolTip() {
	tooltipdiv
		.style("opacity", 0.5)
		.style("visibility", "hidden");
}

function changeUnfocusViewPic() {
	unfocusViewImage
		.attr("xlink:href", "..\\newImages\\backIconSocialGraph_hover.png");
}

function resetUnfocusViewPic() {
	unfocusViewImage
		.attr("xlink:href", "..\\newImages\\backIconSocialGraph.png");
}

function hideUnfocusViewBtn() {
	unfocusViewImage
		.style("visibility", "hidden");
}

function showUnfocusViewBtn() {
	unfocusViewImage
		.style("visibility", "visible");
}


function mouseOver() {

	//if (selectedNodeID ==-1) {
		var el				= d3.select(this);
		var tempid			= el.attr("ID");
		var selectedColor	= el.attr("color");

		showToolTip(el);

		node
			.style("fill",function(d) {
				if (selectedColor==1) {
					return getColorName(tempid, d.ID, xcolor(d.color));
				} else {
					return getColorName(d.ID, tempid, xcolor(d.color));
				}
			});

		el
			.style("fill", xcolor(0));
	//}
}

function getColorName(sourceID, targetID, existingColor) {

	for ( var count=0;count<links.length;count++) {
		if(links[count].source.ID==sourceID && links[count].target.ID==targetID) {
			return xcolor(3);
		}
	}
	return existingColor;
}

function mouseOut() {

	hideToolTip();

	node
		.style("fill",function(d) {
			return xcolor(d.color);
		});
}

function clickWindow() {
	clicks++;
	var el = d3.select(this);
	if (clicks == 1) {
		setTimeout(function() {
			if (clicks == 1) {
				click(el);
			} else {
				dblclick(el);
			}
			clicks = 0;
		}, 300);
	}

}

function click(el) {

	if(el.attr("color")==1) {
		clickHandler(el, 0);
	} else {
		alert(MSG001);
	}
}

function dblclick(el) {

	if (el.attr("color")==1) {
		var objSocialGraph = new socialGraph();
		objSocialGraph.getSelectedCell(el.attr("name"),el.attr("createdDate"));
	} else {
		alert(MSG001);
	}
}

function clickHandler(pNode, isResize) {
	selectedCellName= pNode.attr("name");
	selectedNodeID	= pNode.attr("ID");
	selectedNode	= pNode;  //global reference.

	//fNodestorage.push(selectedCellName);

	setExternalNodesPosition(pNode);
	
	if (isResize==0) {
		response = getCellInfo (selectedCellName);
	}

	focusForceView();
	
	showUnfocusViewBtn();
	
	displayCellProfileImage(response.Image);

	displayCellProfileDesc(response.DisplayName,response.Description);
}

function setSVGDimension() {
	
	width	= parseInt(d3.select("#GraphContainer").style("width")) - parseInt(d3.select("svg").style("margin-left"));
	height	= parseInt(d3.select("#GraphContainer").style("height")) - 60;
	//alert(width + "**" + height);
	}

function setFNodeCenterAndRadius() {
	fNodeR	= (Math.min(width, height) * .80) / 2; //80 percent of smaller side
	fNodeX	= width / 2;
	fNodeY	= (height - 40) / 2;
}

function svgResize() {
	setSVGDimension();
	setFNodeCenterAndRadius();

		svg
			.attr("width", width)
			.attr("height", height);
		if (selectedNodeID==-1) {
			force
				.size([width, height])
				.resume();
		} else {
			clickHandler(selectedNode, 1);
		}
}

function displayCellProfileImage(binaryImage) {
	var imgBinaryFile = binaryImage;
	var img = document.createElement('img');

	document.getElementById('dvUserImage').innerHTML = "";

	img.id		= "imgUserPhoto";
	img.src		= imgBinaryFile;
	img.width	= "124";
	img.height	= "126";
	img.src		= "../newImages/userImage.jpg";

	if (binaryImage!= null && binaryImage!= undefined) {
		img.src	= binaryImage;
	}

	document.getElementById('dvUserImage').appendChild(img);
	
	cellProfileImage
			.attr("x",fNodeX-fNodeR * 0.35)
			.attr("y",fNodeY-fNodeR * 0.90)
			.attr("xlink:href", imgUserPhoto.src)
			.style("visibility","visible");

}

function displayCellProfileDesc(DisplayName,Description) {
	var cellProfile ="<h1><align='center'>" + DisplayName + "</align></h1><p>" + Description;

	d3
			.select("#fo1")
			.attr("x",fNodeX - fNodeR * 0.65)
			.attr("y",fNodeY - fNodeR * 0.90 + IMG_HEIGHT);
	
	cellProfileDesc
			.style("visibility","visible")
			.html(cellProfile);
}

function setExternalNodesPosition(pNode) {
	var count	= 1;
		fRadius	= 1,
		intX	= 1,
		intY	= 1,
		ycount	= 1,
		varID	= pNode.attr("ID"),
		nodeRel	= parseInt(pNode.attr("weight")),
		RemainingWidth = (width - height * 0.80)/2,
		diameter= 1,
		startY	= 1,
		fNodePos= [];
		
	fRadius = getNodeRadius(RemainingWidth, height, 8, nodeRel);
	diameter= 2 * fRadius;
	startY	= diameter;

	ycount	= Math.floor(height/diameter);

	node.each(function(d,i) {
		var lRadius	= 0,
			fPosX	= fNodeX,
			fPosY	= fNodeY;
		if(d.ID==varID) {
			lRadius	= fNodeR;
		}
		for (var j = 0;j <links.length;j++) {
			if(links[j].source.ID==varID && links[j].target.ID==d.ID) {
				lRadius	= fRadius;
				fPosY	= startY * intY;
				fPosX	= diameter * intX;

				if(count%2==0) {
					fPosX	= (width - diameter * intX);
					intY	= intY + 2;
				}
				if(intY>ycount) {
					intX++;
					startY	= diameter;
					intY	= (intX%2==0)? 2 : 1;
				}
				count++;
			}
		}
		fNodePos.push({ID:d.ID,x:fPosX,y:fPosY,r:lRadius});
	});
}

function focusForceView() {

	force
		.on("tick", focusTick)
		.start();
}

function focusTick() {
	fNodeTick();
	fLinkTick();
	cellNameTick();
}

function ufNodeTick() {
	node
		.attr("r", function(d){ return d.size;})
		.attr("cx", function(d) { return d.x = Math.max(d.size, Math.min(width - d.size, d.x)); })
		.attr("cy", function(d) { return d.y = Math.max(d.size, Math.min(height - d.size, d.y)); });
}

function fNodeTick() {
	node
		//.transition()
		//.duration(100)
		.each(function(d,i) {
			//var i =0;
			for (var i=0; i<fNodePos.length; i++) {
				if(fNodePos[i].ID==d.ID) {
					d.x		= fNodePos[i].x;
					d.y		= fNodePos[i].y;
					d.size	= fNodePos[i].r;
					break;
				}
			}
		});
	ufNodeTick();

}

function ufLinkTick() {
	link
		//.transition()
		//.duration(100)
		.attr("x1", function(d) { return d.source.x;})
		.attr("y1", function(d) { return d.source.y;})
		.attr("x2", function(d) { return d.target.x;})
		.attr("y2", function(d) { return d.target.y;});
}

function fLinkTick() {
	link
		//.transition()
		//.duration(100)
		.attr("x1", function(d) {
			if(selectedNodeID == d.source.ID) {
				return d.source.x;
			}else{
				return d.target.x;
			}
		})
		.attr("y1", function(d) {
			if(selectedNodeID == d.source.ID) {
				return d.source.y;
			}else{
				return d.target.y;
			}
		})
		.attr("x2", function(d) { return d.target.x;})
		.attr("y2", function(d) { return d.target.y;});
}

function cellNameTick() {
	cellName
		//.transition()
		//.duration(100)
		.attr("x", function(d) {
			if((d.x==fNodeX && d.y==fNodeY) || d.size<=SHOW_CELLNAME) {
				return HIDE_POSITION;
			} else {
				return d.x;
			}
		})
		.attr("y", function(d) {
			if((d.x==fNodeX && d.y==fNodeY)||d.size<=SHOW_CELLNAME) {
				return HIDE_POSITION;
			} else {
				return d.y;
			}
		})
		.attr("text", function(d){ return d.name;});
}

function getArrayNodes() {

	for ( var i = 0; i < lenJSONstring; i++) {
		var obj = jsonString[i];
		if (!obj) {
		} else {
			var cellName = jsonString[i].Name;
			var cellDate = jsonString[i].__published;
		}
		var node = {
			name			: cellName,
			color			: 1,
			size			: ufRadius,
			cellCreatedDate	: objCommon.convertEpochDateToReadableFormat(cellDate),
			ID				: i
		};

		nodes.push(node);
	}
}

function getArrayOfExtNodes() {
	var Cellcount=nodes.length;
	for ( var i = 0; i < nodes.length; i++) {
		var json			= retrieveExternalList(nodes[i].name);
		var jsonString		= json.rawData;
		var recordSize		= jsonString.length;
		var addExternalCell	= 1;
		var cellDate;

		if(jsonString[i] != undefined) {
			 cellDate = jsonString[i].__published;
		}
		for ( var count = 0; count < recordSize; count++) {
			
			var arrayData		= jsonString[count];
			var externalCellName= getExternalCellNameForSocialGraph(arrayData.Url);
			
			if (!externalCellName) {

			} else if(externalCellName !=undefined) {
				//alert(nodes[i].name + "++++" + externalCellName);
				for ( var count1 = 0; count1 < nodes.length; count1++) {
					if(nodes[count1].name===externalCellName) {
						addExternalCell=0;
						break;
					}
				}
				
				if (addExternalCell == 1) {
					for ( var count1 = 0; count1 < Extnodes.length; count1++) {
						if(Extnodes[count1].name===externalCellName) {
							addExternalCell=0;
							break;
						}
					}
				}
					
				if (addExternalCell==1) {
					//alert("included" + externalCellName);
					var extNode = {
								name 			: externalCellName,
								color			: 2,
								size			: ufRadius,
								cellCreatedDate	: objCommon.convertEpochDateToReadableFormat(cellDate),
								ID				: Cellcount
					};
					Cellcount = Cellcount + 1;
					Extnodes.push(extNode);
					lenJSONstring = lenJSONstring + 1;
				}
				addExternalCell = 1;
				
				var extCellLink = {
							cellName	: nodes[i].name,
							extCellName	: externalCellName
				};
				extNodesArray.push(extCellLink);

			}
		}
	}
	
	for (var i = 0; i < Extnodes.length; i++) {
		nodes.push(Extnodes[i]);
	}

}

function getArrayOfLinks() {
	for ( var i = 0; i < nodes.length; i++) {
		for ( var j = 0; j < extNodesArray.length; j++) {
			if (nodes[i].name === extNodesArray[j].cellName) {
				for(var cellCount=0 ; cellCount<nodes.length; cellCount++) {
					if (extNodesArray[j].extCellName === nodes[cellCount].name ) {
						links.push({
							source	: i,
							target	: cellCount,
							relation: "friend",
							bond	: 1
						});
					}
				}
			}
		}
	}
}

function createMarker(){
	// build the arrow.
	svg
		.append("svg:defs")
		.selectAll("marker")
		.data([ "mid" ])
		.enter()
		.append("svg:marker")
		.attr("id", String)
		.attr("viewBox", "0 -5 10 10")
		.attr("refX", 10)
		.attr("refY", 0)
		.attr("markerWidth", 6)
		.attr("markerHeight",6)
		.attr("orient", "auto")
		.append("svg:path")
		.attr("d", "M0,-5L10,0L0,5");
}

function hideCellProfileInfo() {
	cellProfileImage
		.style("visibility","hidden");

	cellProfileDesc
		.style("visibility","hidden");
}

function movePrevious() {

	hideCellProfileInfo();
	
	hideUnfocusViewBtn();
/*
	fNodestorage.pop();

	if (fNodestorage.length>0) {
		clickcell(fNodestorage.pop())
		return;
	}
*/
	selectedNodeID = -1; //unfocus view should not have any selected cell.

	selectedNode	= "";

	resetNodeSize();

	unfocusForceView();

}

function resetNodeSize() {
	node
		.each(function(d) {
			d.size = ufRadius;
		});	
}

function clickcell(cellName) {
	//No need to check the Unit Cell condition as all cells in the search it gives the top result and Node array contains the all Unit cells first and then contain
	//external cell data.
	var nodeObject = node
		.filter(function(d) {
			if(d.name==cellName) { 
				return true;
		};
	});

	clickHandler(nodeObject, 0);
}

function getCellList() {
	var baseUrl			= sessionStorage.selectedUnitUrl;
	var objJdcContext	= new _pc.PersoniumContext(baseUrl, "", "", "");
	var accessor		= objJdcContext.withToken(token);
	var results			= accessor.asCellOwner().unit.cell.query().top(1002).run();
	var cellList		= JSON.stringify(results);

	return cellList;
}

function retrieveExternalList(cellname) {
	var baseUrl				= sessionStorage.selectedUnitUrl;
	var externalCellURI 	= new Array();
	var objJdcContext		= new _pc.PersoniumContext(baseUrl, cellname, "", "");
	var accessor			= objJdcContext.withToken(token);
	var objExtCellManager	= new _pc.ExtCellManager(accessor);
	var json				= objExtCellManager.retrieve("");

	return json;
}

function getExternalCellNameForSocialGraph(uri) {
	var arrUri				= uri.split("/");
	var externalCellName	= arrUri[3];
	return externalCellName;
}

function getCellInfo (cellName) {
	var profileFileName	= "profile.json";
	var baseUrl			= sessionStorage.selectedUnitUrl;
	var response		= null;
	if (!baseUrl.endsWith("/")) {
		baseUrl += "/";
	}
	var path				= baseUrl+cellName+"/__/";
	var objJPersoniumContext		= new _pc.PersoniumContext(baseUrl, cellName);
	var accessor			= objJPersoniumContext.withToken(token);
	var objJDavCollection	= new _pc.DavCollection(accessor, path);
	response				= objJDavCollection.getJSON(profileFileName);
	return response.bodyAsJson();
}