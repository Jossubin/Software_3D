document.addEventListener('DOMContentLoaded', function() {
    // 상품 ID 가져오기
    const productIdElement = document.getElementById('productId');
    if (!productIdElement) {
        console.error('Product ID element not found');
        return;
    }
    const productId = productIdElement.value;

    // 수량 입력 제한
    const quantityInput = document.getElementById('quantity');
    if (quantityInput) {
        quantityInput.addEventListener('input', function() {
            if (this.value < 1) this.value = 1;
        });
    }

    // 장바구니 추가 버튼
    const addToCartBtn = document.querySelector('.add-to-cart');
    if (addToCartBtn) {
        addToCartBtn.addEventListener('click', function() {
            const quantity = document.getElementById('quantity').value;
            const selectedSize = document.querySelector('input[name="size"]:checked')?.value;
            const selectedColor = document.querySelector('input[name="color"]:checked')?.value;

            if (!selectedSize || !selectedColor) {
                alert('사이즈와 색상을 선택해주세요.');
                return;
            }

            fetch('/cart/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    productId: productId,
                    quantity: quantity,
                    size: selectedSize,
                    color: selectedColor
                })
            })
            .then(response => {
                if (response.ok) {
                    if (confirm('상품이 장바구니에 추가되었습니다.\n장바구니로 이동하시겠습니까?')) {
                        window.location.href = '/cart';
                    }
                } else {
                    response.text().then(errorMessage => {
                        if (response.status === 401) {
                            if (confirm('로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?')) {
                                window.location.href = '/login';
                            }
                        } else {
                            alert(errorMessage || '장바구니 추가에 실패했습니다.');
                        }
                    });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('장바구니 추가 중 오류가 발생했습니다.');
            });
        });
    }

    // 하트 버튼
    const heartBtn = document.querySelector('.heart-button');
    if (heartBtn) {
        heartBtn.addEventListener('click', function() {
            this.classList.toggle('active');
            alert('찜 기능은 준비중입니다.');
        });
    }

    // 3D 버튼들
    const view3dBtn = document.querySelector('.view-3d-button');
    if (view3dBtn) {
        view3dBtn.addEventListener('click', function() {
            alert('3D Fitting 기능 준비중입니다.');
        });
    }

    const view3DBtn = document.querySelector('.3D-button');
    if (view3DBtn) {
        view3DBtn.addEventListener('click', function() {
            alert('3D View 기능 준비중입니다.');
        });
    }
}); 