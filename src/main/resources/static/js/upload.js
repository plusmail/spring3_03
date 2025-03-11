

async function uploadToServer(formObj) {

    console.log("uploadToServer실행");
    console.log(formObj);

    const response = await axios({
        method: 'POST',
        url: '/upload',
        data: formObj,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });

    return response.data;

}

async function removeFileToServer(uuid, fileName) {
    const response = await axios.delete(`/remove/${uuid}_${fileName}`);

    return response.data;
}