let vueInstance = Vue.createApp({
    data() {
        return {
            message: 'Hello World',
            counter: 0,
            clientX: 0,
            clientY: 0,
        }
    },
    mounted() {
        setTimeout(() => {
            this.message = 'Changed after 3 seconds~';
        }, 3000);
    },
    methods: {
        increase() {
            this.counter++;
        },
        handleMouseMove(e) {
            this.clientX = e.clientX;
            this.clientY = e.clientY;
        },
        handleSubmit(e) {
            console.log(e);
        }
    }
});

vueInstance.mount('#app');