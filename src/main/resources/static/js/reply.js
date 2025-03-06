

async function get1(bno) {
    const result = await axios.get(`/replies/list/${bno}`);

    return result;
}
// axios 비동기 통신 함수
// async, await
// 호출 후 성공하면 then, 실패하면 catch실햄
async function getList({bno, page, size, goLast, goFirst}) {
    const result = await axios.get(`/replies/list/${bno}`,{params: {page,size}});

    if(goLast){
        const total = result.data.total;
        const lastPage = parseInt(Math.ceil(total/size));
        return getList({bno:bno, page:lastPage, size:size});
    }

    if(goFirst){
        return  getList({bno:bno, page:1, size:size});
    }

    return result.data;
}

async function addReply(replyObj){
    const response = await axios.post(`/replies/`, replyObj);
    return response;
}

async function getReply(rno){
    const response = await axios.get(`/replies/${rno}`);
    return response.data;
}

async function modifyReply(replyObj){
    const response =
        await axios.put(`/replies/${replyObj.rno}`, replyObj);
    return response.data;
}

async function removeReply(rno){
    const response = await axios.delete(`/replies/${rno}`);
    return response.data;
}