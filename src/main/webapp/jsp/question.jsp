<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" ></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<script type="text/x-mathjax-config">
      MathJax.Hub.Config({
        messageStyle: "none",
        tex2jax: {inlineMath: [["$","$"],["\\(","\\)"]]}
      });
    </script>

<script type="text/javascript"
	src="${ctx}/js/MathJax.js?config=TeX-AMS_HTML-full"></script>

<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>

<style type='text/css'>
            .optionLine { border-bottom:5px solid #f0f0f0; padding:10px 2px; font-size:16px; line-height:30px;}
            .blueContent{color:#0099FF; }
            input[type="radio"] {
                -webkit-appearance: none;
                height: 24px;
                vertical-align: middle;
                width: 24px;
                border-width:0px;
            }
            input[type="radio"]:checked {
            	outline:none;
                background: #fff url(ic_selected.png) no-repeat;
                background-size:contain;
            }
            input[type="radio"]:not(:checked){
            	outline:none;
                background: #fff url(ic_unselect.png) no-repeat;
                background-size:contain;
            }
            .center {
                text-align:center
            }
            .auto_image { max-height:120px; max-width:320px;
                autoimg:expression(onload=function(){this.style.width=(this.offsetWidth > 320)?"320px":"auto"});
            }
            .button {
                display: inline-block;
                width: 100%;
                outline: none;
                cursor: pointer;
                text-align: center;
                text-decoration: none;
                color: #fff;
                font: 14px/100% Arial, Helvetica, sans-serif;
                padding: .5em 2em .55em;
                text-shadow: 0 1px 1px rgba(0,0,0,.3);
                -webkit-border-radius: .5em;
                -moz-border-radius: .5em;
                border-color: #0099ff;
                border-radius: .5em;
                -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.2);
                -moz-box-shadow: 0 1px 2px rgba(0,0,0,.2);
                box-shadow: 0 1px 2px rgba(0,0,0,.2);
                background: #0099ff;
            }
            .button:hover {
                text-decoration: none;
            }
            .button:active {
                position: relative;
                top: 1px;
            }
</style>

<script type="text/javascript">

	var objectId = <%=request.getParameter("objectId")%>
	var type = <%=request.getParameter("type")%>
	
	var questionList;
	var currentIndex;
	var answerArray; // 答题结果记录
	var showAnswer; //是否直接显示答案
	var answerCorrect; //用户所选是否正确
	
	function loadQuestion(showAnswer){
		this.showAnswer = showAnswer;
		this.currentIndex = 0;
		if(type==1){
			$.ajax({
				type : 'POST',
				url : '${ctx}/cour/getPoiSubList',
				data : {
					"pointId" : objectId
				},
				success : function(data) {
					questionList = data.result;
					if(answerArray==null){
                        answerArray = new Array(questionList.length);
                	}
					window.AndroidWebView.updateQuestionIndex('1/'+questionList.length);
					updateMath(questionList[currentIndex]);
				},
				dataType : "JSON"
			});
		}else if(type==2){
			$.ajax({
				type : 'POST',
				url : '${ctx}/exam/getExamSubList',
				data : {
					"examId" : objectId
				},
				success : function(data) {
					questionList = data.result;
					if(answerArray==null){
                        answerArray = new Array(questionList.length);
                	}
					window.AndroidWebView.updateQuestionIndex('1/'+questionList.length);
					updateMath(questionList[currentIndex]);
				},
				dataType : "JSON"
			});
		}else if(type==3){
			var userId = <%=request.getParameter("userId")%>
			$.ajax({
				type : 'POST',
				url : '${ctx}/cour/getWrongSubSet',
				data : {
					"cid" : objectId,
					"type" : "1",
					"userid": userId
				},
				success : function(data) {
					questionList = data.result;
					if(answerArray==null){
                        answerArray = new Array(questionList.length);
                	}
					window.AndroidWebView.updateQuestionIndex('1/'+questionList.length);
					updateMath(questionList[currentIndex]);
				},
				dataType : "JSON"
			});
		}
	}

	function updateMath(data) {
		
		// 视频讲解
		window.AndroidWebView.showVideo(data.videourl);

		var questionDIV = document.getElementById("question");
		questionDIV.className = 'optionLine';
		questionDIV.innerHTML = data.question;
		MathJax.Hub.Queue([ "Typeset", MathJax.Hub, questionDIV ]);
		
		//选项A
        var optionDIV1 = document.getElementById('option1');
        optionDIV1.innerHTML = 'A. '+ data.optionList[0].content;
        MathJax.Hub.Queue(["Typeset",MathJax.Hub,optionDIV1]);
        //选项B
        var optionDIV2 = document.getElementById('option2');
        optionDIV2.innerHTML = 'B. '+ data.optionList[1].content;
        MathJax.Hub.Queue(["Typeset",MathJax.Hub,optionDIV2]);
        //选项C
        if(data.optionList[2]!=null){
            document.getElementById('line3').style.display="";
            document.getElementById('divider3').style.display="";
            var optionDIV3 = document.getElementById('option3');
            optionDIV3.innerHTML = 'C. '+ data.optionList[2].content;
            MathJax.Hub.Queue(["Typeset",MathJax.Hub,optionDIV3]);
        }else{
            document.getElementById('line3').style.display="none";
            document.getElementById('divider3').style.display="none";
        }
        
        //选项D
        if(data.optionList[3]!=null){
            document.getElementById('line4').style.display="";
            document.getElementById('divider4').style.display="";
            var optionDIV4 = document.getElementById('option4');
            optionDIV4.innerHTML = 'D. '+ data.optionList[3].content;
            MathJax.Hub.Queue(["Typeset",MathJax.Hub,optionDIV4]);
        }else{
            document.getElementById('line4').style.display="none";
            document.getElementById('divider4').style.display="none";
        }
        
        //选项E
        if(data.optionList[4]!=null){
            document.getElementById('line5').style.display="";
            document.getElementById('divider5').style.display="";
            var optionDIV5 = document.getElementById('option5');
            optionDIV5.innerHTML = 'E. '+ data.optionList[4].content;
            MathJax.Hub.Queue(["Typeset",MathJax.Hub,optionDIV5]);
        }else{
            document.getElementById('line5').style.display="none";
            document.getElementById('divider5').style.display="none";
        }
        
      	//选中
      	var i=0;
            $("[name='radio']").each(function(){
            	if(answerArray!=null&&answerArray[currentIndex]!=null){
              		var obj = answerArray[currentIndex];
              		if(obj.currentChoice!=null&&i++==obj.currentChoice){
                        $(this).prop("checked",true);
                     }else{
                        $(this).prop("checked",false);
                     }
              	}else{
              		$(this).prop("checked",false);
              	}                             
		})
        
      	//图片
        var questionImage = document.getElementById("img");
        if(data.pic!=null&&data.pic.length>0){
            questionImage.style.display="";
            questionImage.src = data.pic;
        }else{
            questionImage.style.display="none";
        }
		
		//答案
        var answerDIV = document.getElementById('answer');
		var rightAnswer;
		for(var j=0;j<data.optionList.length;j++){
			if(data.optionList[j].isanswer==1){
				rightAnswer=65+j;
			}
		}
        answerDIV.innerHTML = '正确答案: <span class="blueContent">'+ String.fromCharCode(rightAnswer)+'</span>'; //ascii码砖字符
     
        //解析
        var analysisDIV = document.getElementById('analysis');
        analysisDIV.innerHTML = data.hint;
        MathJax.Hub.Queue(["Typeset",MathJax.Hub,analysisDIV]);
        
      	//解析图片
        var hintImage = document.getElementById("hintimg");
        hintImage.src = data.hintpic;
        
        if(showAnswer=='1'){
            document.getElementById("showBtn").style.display="none";
            var correctDIV  = document.getElementById("answerCorrect");
            if(type=='2'){
                correctDIV.style.display="";
                var answer = answerArray[currentIndex];
          		if(answer!=null&&answer.currentChoice==rightAnswer-65){
          			correctDIV.innerHTML = '答案正确';
                    correctDIV.style.color = "#0099ff";
          		}else{
                    correctDIV.innerHTML = '答案错误';
                    correctDIV.style.color = "red";
                }
            }
            document.getElementById("answer").style.display="";
            document.getElementById("analysis").style.display="";
            if(data.hintpic.length>0){
                hintImage.style.display="";
                hintImage.src = data.hintpic;
            }else{
                hintImage.style.display="none";
            }
        }else{
            if(type=='1'){
                document.getElementById("showBtn").style.display="";
            }else{
                document.getElementById("showBtn").style.display="none";
            }
            hintImage.style.display="none";
            document.getElementById("answer").style.display="none";
            document.getElementById("analysis").style.display="none";
        }
     	// 英语文章
		if(data.article.length>0){
			window.AndroidWebView.showArticle(data.article);
		}
	}
	//上一题目
	function clickPrevious(){
		if(currentIndex>0){
			window.AndroidWebView.updateQuestionIndex(currentIndex+'/'+questionList.length);
			if(currentIndex==1){
				//调用android中的方法
				window.AndroidWebView.hidePreviousButton();
			}
			if(currentIndex==questionList.length-1){
				window.AndroidWebView.updateNextButton("下一题");
			}
			currentIndex--;
			updateMath(questionList[currentIndex]);
		}
	}
	
	//下一题
	function clickNext(){
		//调用android中的方法
		if(currentIndex==0){
			window.AndroidWebView.showPreviousButton();
		}
		//选中的答案
		var optionIndex=$('input:radio[name="radio"]:checked').val();
		
		var answerObj = new Object();
		answerObj.questionId = questionList[currentIndex].id;
		if(optionIndex!=null){
			answerObj.optionId = questionList[currentIndex].optionList[optionIndex].id;
			answerObj.isCorrect = questionList[currentIndex].optionList[optionIndex].isanswer;
			answerObj.currentChoice = optionIndex;
		}else{
			answerObj.optionId = '';
			answerObj.isCorrect = '2'; //未选择
		}
		answerArray[currentIndex] = answerObj;
		
		if(currentIndex<questionList.length-1){
			if(currentIndex==questionList.length-2){
				window.AndroidWebView.updateNextButton("提交");
			}
			currentIndex++;
			window.AndroidWebView.updateQuestionIndex((currentIndex+1)+'/'+questionList.length);
			updateMath(questionList[currentIndex]);
		}else{
			window.AndroidWebView.submitAnswer(JSON.stringify(answerArray).toString());
		}
	}
	//显示答案
	function show(){
		document.getElementById("showBtn").style.display="none";
		document.getElementById("answer").style.display="";
		document.getElementById("analysis").style.display="";
	}

</script>

</head>
<body>

	<div id='question'></div>
    
    <table style="width: 100%; border: 0px">
        <tr>
            <td align='left'><div id='option1' /></td>
            <td align='right'><input type='radio' name='radio' value='0'/></td>
        </tr>
        <tr>
            <td colspan='2'><hr color="#f0f0f0" size="5" width="100%" /></td>
        </tr>
        <tr>
            <td align='left'><div id='option2' /></td>
            <td align='right'><input type='radio' name='radio' value='1'/></td>
        </tr>
        <tr>
            <td colspan='2'><hr color="#f0f0f0" size="5" width="100%" /></td>
        </tr>
        <tr id='line3'>
            <td align='left'><div id='option3' /></td>
            <td align='right'><input type='radio' name='radio' value='2'/></td>
        </tr>
        <tr id='divider3'>
            <td colspan='2'><hr color="#f0f0f0" size="5" width="100%" /></td>
        </tr>
        <tr id='line4'>
            <td align='left'><div id='option4' /></td>
            <td align='right'><input type='radio' name='radio' value='3'/></td>
        </tr>
        <tr id='divider4'>
            <td colspan='2'><hr color="#f0f0f0" size="5" width="100%" /></td>
        </tr>
        <tr id='line5'>
            <td align='left'><div id='option5' /></td>
            <td align='right'><input type='radio' name='radio' value='4'/></td>
        </tr>
        <tr id='divider5'>
            <td colspan='2'><hr color="#f0f0f0" size="5" width="100%" /></td>
        </tr>
        
    </table>
    
    <div class="center">
        <img class="auto_image" id="img"/>
    </div>
	
	<div>
        <button id="showBtn" class="button"  style="display: none" onclick="show()">显示答案及解析</button>
        <div id='answerCorrect' style="display: none"></div>
        <div id='answer' style="display: none"></div>
        <div id='analysis' style="line-height:30px; display: none"></div>
        <div class="center">
            <img id="hintimg" class="auto_image" style="display: none"/>
        </div>
    </div>
	
</body>
</html>