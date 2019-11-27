<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="ishop" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div id="productList" data-page-count="${pageCount}" data-page-number="1"> <%--сохраняем текущую страницу которая отображена и общее кол-во pageCount для того чтобы с помощью ajax подгружать еще новую порцию. связана с кнопкой  Load more products. и с функцией js --%>
    <div class="row">
        <jsp:include page="../fragment/product-list.jsp" />
    </div>
    <c:if test="${pageCount > 1}"> <%--pageCount из класса AllProductsController. если больше 1 то отображаем кнопку --%>
    <div class="text-center hidden-print">
        <a id="loadMore" class="btn btn-success">Load more products</a>
    </div>
    </c:if>
</div>
<ishop:add-product-popup />
