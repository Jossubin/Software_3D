let currentIndex = 0;
const slides = document.querySelectorAll('.slide');
const slideInterval = 5000; // 슬라이드 전환 간격 (밀리초)
const heroText = document.querySelector('.hero-text');

function showSlide(index) {
    slides.forEach((slide, i) => {
        slide.classList.remove('active');
        if (i === index) {
            slide.classList.add('active');
        }
    });

    // 애니메이션을 위한 클래스 추가
    heroText.classList.remove('animate', 'left', 'right'); // 초기화
    if (index === 0) {
        heroText.classList.add('left', 'animate'); // 첫 번째 슬라이드: 왼쪽 위치
    } else if (index === 1) {
        heroText.classList.add('right', 'animate'); // 두 번째 슬라이드: 오른쪽 위치
    }
    // 추가 슬라이드가 있다면 여기에 조건을 추가하세요
    // 예를 들어, 세 번째 슬라이드가 중앙에 위치해야 한다면:
    // else if (index === 2) {
    //     heroText.classList.add('center', 'animate'); // 별도의 CSS 클래스 필요
    // }
}

function nextSlide() {
    currentIndex = (currentIndex + 1) % slides.length;
    showSlide(currentIndex);
}

if (slides.length > 0) {
    slides[0].classList.add('active');
    showSlide(currentIndex); // 초기 슬라이드 표시
    setInterval(nextSlide, slideInterval); // 슬라이드 자동 전환
}

// 피팅 기능을 함수로 분리
function handleFitting() {
    const loader = document.getElementById("loader");
    const productImage = document.querySelector(".product-image img");
    const productTitle = document.querySelector(".product-title").textContent;
    
    // 원본 이미지 경로에서 피팅 이미지 경로 생성
    const originalSrc = productImage.src;
    const fitImageSrc = originalSrc.replace(/(\.[^.]+)$/, '_fit$1');
    
    // 기존 fitting-result가 있다면 제거
    const existingResult = document.querySelector('.fitting-result');
    if (existingResult) {
        existingResult.remove();
    }

    // 로딩 애니메이션 표시
    loader.style.display = "flex";

    // 3초 후에 이미지 확인 및 결과 표시 시작
    setTimeout(() => {
        const checkImage = new Image();
        checkImage.onload = () => {
            loader.style.display = "none";
            const resultSection = document.createElement("div");
            resultSection.classList.add("fitting-result", "modal-overlay");
            resultSection.innerHTML = `
                <div class="fitting-container modal-content">
                    <h1>Fitting Result</h1>
                    <div class="fitting-image-container">
                        <img src="${fitImageSrc}" alt="${productTitle} Fitting Result">
                    </div>
                    <p>Here is the preview of your fitting result.</p>
                    <button class="back-button">Back to Product</button>
                </div>
            `;

            document.body.appendChild(resultSection);
            
            // 버튼 클릭 이벤트 추가
            resultSection.querySelector('.back-button').addEventListener('click', () => {
                resultSection.remove();
            });
        };

        checkImage.onerror = () => {
            loader.style.display = "none";
            const resultSection = document.createElement("div");
            resultSection.classList.add("fitting-result", "modal-overlay");
            resultSection.innerHTML = `
                <div class="fitting-container modal-content">
                    <h1>Fitting Result</h1>
                    <div class="fitting-image-container">
                        <img src="${originalSrc}" alt="${productTitle} Fitting Result">
                    </div>
                    <p>피팅 이미지를 찾을 수 없습니다.</p>
                    <button class="back-button">Back to Product</button>
                </div>
            `;
            
            document.body.appendChild(resultSection);
            
            // 버튼 클릭 이벤트 추가
            resultSection.querySelector('.back-button').addEventListener('click', () => {
                resultSection.remove();
            });
        };

        checkImage.src = fitImageSrc;
    }, 3000); // 3초 대기
}

// 아바타 아이콘과 피팅 버튼에 이벤트 리스너 추가
document.getElementById("avata-icon")?.addEventListener("click", handleFitting);
document.getElementById("fitting-button")?.addEventListener("click", handleFitting);

// 3D 뷰 기능을 함수로 분리
function handle3DView() {
    console.log("3D View 함수 실행");
    const loader = document.getElementById("loader");
    const productImage = document.querySelector(".product-image img");
    const productTitle = document.querySelector(".product-title").textContent;
    
    // 이미지 경로에서 파일명 추출 로직 수정
    const originalSrc = productImage.src;
    console.log("Original image source:", originalSrc);
    
    try {
        // product_IMG_ 이후의 파일명만 추출
        const matches = originalSrc.match(/product_IMG_\/(.*?)\.[^.]+$/);
        console.log("정규식 매치 결과:", matches); // 매치 결과 확인
        
        if (!matches || !matches[1]) {
            throw new Error('파일명을 추출할 수 없습니다.');
        }
        
        const fileName = matches[1];
        console.log("추출된 파일명:", fileName);
        
        // OBJ 파일 경로 수정
        const model3dSrc = `/obj/${fileName}_3d.obj`;
        console.log("생성된 3D 모델 경로:", model3dSrc);
        
        // 바로 3D 뷰어 초기화
        console.log("3D model file found, initiating viewer...");
        initiate3DViewer(model3dSrc);
        
    } catch (error) {
        console.error('Error in handle3DView:', error);
        alert('3D 모델을 불러올 수 없습니다. 파일 경로를 확인해주세요.');
        loader.style.display = "none";
    }
}

// 3D 뷰 버튼에 이벤트 리스너 추가
document.getElementById("view-3d-button")?.addEventListener("click", handle3DView);

// 슬라이드 쇼 관련 코드는 홈페이지에서만 필요하다면 조건부로 실행
if (document.querySelector('.slide')) {
    let currentIndex = 0;
    const slides = document.querySelectorAll('.slide');
    const slideInterval = 5000;
    const heroText = document.querySelector('.hero-text');

    function showSlide(index) {
        slides.forEach((slide, i) => {
            slide.classList.remove('active');
            if (i === index) {
                slide.classList.add('active');
            }
        });

        // 애니메이션을 위한 클래스 추가
        heroText.classList.remove('animate', 'left', 'right');
        if (index === 0) {
            heroText.classList.add('left', 'animate');
        } else if (index === 1) {
            heroText.classList.add('right', 'animate');
        }
    }

    function nextSlide() {
        currentIndex = (currentIndex + 1) % slides.length;
        showSlide(currentIndex);
    }

    if (slides.length > 0) {
        slides[0].classList.add('active');
        showSlide(currentIndex);
        setInterval(nextSlide, slideInterval);
    }
}

function initiate3DViewer(modelPath) {
    // 로딩 표시
    const loader = document.getElementById("loader");
    loader.style.display = "flex";

    // 기존 3D 뷰어가 있다면 제거
    const existingViewer = document.querySelector('.threejs-viewer');
    if (existingViewer) {
        existingViewer.remove();
    }

    // 3초 후에 3D 뷰어 표시
    setTimeout(() => {
        // 뷰어 컨테이너 생성
        const container = document.createElement('div');
        container.classList.add('threejs-viewer', 'modal-overlay');
        container.innerHTML = `
            <div class="modal-content" style="width: 800px; height: 650px; position: relative;">
                <div id="3d-canvas" style="width: 100%; height: 90%;"></div>
                <div style="text-align: center; padding: 15px;">
                    <button class="back-button">Back to Product</button>
                </div>
            </div>
        `;
        document.body.appendChild(container);

        // Three.js 초기화
        const scene = new THREE.Scene();
        scene.background = new THREE.Color(0xf0f0f0);
        
        const camera = new THREE.PerspectiveCamera(75, 800 / 600, 0.1, 1000);
        camera.position.z = 5;

        const renderer = new THREE.WebGLRenderer({ antialias: true });
        renderer.setSize(800, 600);
        document.getElementById('3d-canvas').appendChild(renderer.domElement);

        // 조명 추가
        const ambientLight = new THREE.AmbientLight(0xffffff, 0.5);
        scene.add(ambientLight);
        
        const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8);
        directionalLight.position.set(1, 1, 1);
        scene.add(directionalLight);

        // OrbitControls 추가
        const controls = new THREE.OrbitControls(camera, renderer.domElement);
        controls.enableDamping = true;
        controls.dampingFactor = 0.05;

        // OBJ 로더
        const objLoader = new THREE.OBJLoader();
        objLoader.load(
            modelPath,
            function (object) {
                const box = new THREE.Box3().setFromObject(object);
                const size = box.getSize(new THREE.Vector3());
                const maxSize = Math.max(size.x, size.y, size.z);
                const scale = 3 / maxSize;
                object.scale.set(scale, scale, scale);

                const center = box.getCenter(new THREE.Vector3());
                object.position.sub(center.multiplyScalar(scale));

                scene.add(object);
                loader.style.display = "none";  // 로딩 숨기기
            },
            function (xhr) {
                console.log((xhr.loaded / xhr.total * 100) + '% loaded');
            },
            function (error) {
                console.error('An error happened:', error);
                loader.style.display = "none";
                alert('3D 모델을 불러오는 중 오류가 발생했습니다.');
            }
        );

        // 애니메이션 루프
        function animate() {
            requestAnimationFrame(animate);
            controls.update();
            renderer.render(scene, camera);
        }
        animate();

        // 돌아가기 버튼 이벤트
        container.querySelector('.back-button').addEventListener('click', () => {
            container.remove();
        });

    }, 3000); // 3초 대기
}

// CSS 스타일 수정
const style = document.createElement('style');
style.textContent = `
    .threejs-viewer.modal-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.7);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 1000;
    }
    .back-button {
        padding: 10px 20px;
        background: #4a90e2;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 16px;
    }
    .back-button:hover {
        background: #357abd;
    }
`;
document.head.appendChild(style);