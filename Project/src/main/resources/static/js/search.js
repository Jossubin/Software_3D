document.addEventListener('DOMContentLoaded', function() {
    const searchIcon = document.getElementById('navSearchIcon');
    const searchForm = document.querySelector('.nav-search');
    
    searchIcon.addEventListener('click', function(e) {
        e.preventDefault();
        if (searchForm.style.display === 'none') {
            searchForm.style.display = 'block';
            searchForm.querySelector('input').focus();
        } else {
            searchForm.style.display = 'none';
        }
    });

    // 검색창 외부 클릭시 닫기
    document.addEventListener('click', function(e) {
        if (!searchForm.contains(e.target) && !searchIcon.contains(e.target)) {
            searchForm.style.display = 'none';
        }
    });
}); 