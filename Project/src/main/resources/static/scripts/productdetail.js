document.querySelector('.add-to-cart').addEventListener('click', function() {
    const productId = window.location.pathname.split('/').pop();
    const quantity = document.getElementById('quantity').value;
    const size = document.querySelector('input[name="size"]:checked')?.value;
    const color = document.querySelector('input[name="color"]:checked')?.value;

    if (!size) {
        alert('사이즈를 선택해주세요.');
        return;
    }
    if (!color) {
        alert('색상을 선택해주세요.');
        return;
    }

    fetch('/add-to-cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            productId: productId,
            quantity: quantity,
            size: size,
            color: color
        })
    })
    .then(response => {
        if (response.ok) {
            alert('장바구니에 추가되었습니다.');
        } else if (response.status === 401) {
            const currentPath = window.location.pathname;
            window.location.href = `/login?redirect=${encodeURIComponent(currentPath)}`;
        } else {
            alert('장바구니 추가에 실패했습니다.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('장바구니 추가 중 오류가 발생했습니다.');
    });
});

// 색상 선택 기능 수정
document.querySelectorAll('input[name="color"]').forEach(radio => {
    radio.addEventListener('change', function() {
        // 모든 color span에서 selected 클래스 제거
        document.querySelectorAll('.color').forEach(c => c.classList.remove('selected'));
        // 선택된 라디오 버튼의 다음 형제 요소(span.color)에 selected 클래스 추가
        this.nextElementSibling.classList.add('selected');
    });
}); 