import { S3Client, PutObjectCommand } from '@aws-sdk/client-s3';
import { useEffect, useMemo, useRef } from 'react';
import ReactQuill from 'react-quill-new';
import 'react-quill-new/dist/quill.snow.css';
import './MyEditor.css';

import propTypes from 'prop-types';

const REGION = import.meta.env.VITE_AWS_S3_BUCKET_REGION;
const ACCESS_KEY = import.meta.env.VITE_AWS_S3_BUCKET_ACCESS_KEY_ID;
const SECRET_ACCESS_KEY = import.meta.env.VITE_AWS_S3_BUCKET_SECRET_ACCESS_KEY;

function MyEditor({ funding, onChange, setRegistActive }) {
  const editorRef = useRef(null);

  useEffect(() => {
    const isEmptyContent =
      !funding.content || funding.content === '<p><br></p>';
    setRegistActive(!isEmptyContent);
    console.log(funding.content);
  }, [funding.content]);

  const handleChange = (value) => {
    // content 값이 변경될 때 onChange를 호출하여 funding 객체를 업데이트
    onChange({ target: { id: 'content', value } });
  };

  const handleImageUpload = (file, callback) => {
    const reader = new FileReader();
    reader.onload = (e) => {
      const base64 = e.target.result;
      callback(base64, '이미지 설명');
    };
    reader.readAsDataURL(file);
  };

  const imageHandler = async () => {
    const input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.setAttribute('accept', 'image/*');
    input.click();
    input.addEventListener('change', async () => {
      // 이미지를 담아 전송할 file
      const file = input.files?.[0];
      try {
        // 생성한 s3 관련 설정들
        const s3Client = new S3Client({
          credentials: {
            secretAccessKey: SECRET_ACCESS_KEY,
            accessKeyId: ACCESS_KEY,
          },
          region: REGION,
        });

        const params = {
          Bucket: 'hackerton',
          Key: `upload/${Date.now()}_${file.name}`, // 파일 이름을 추가하여 고유한 키 생성
          Body: file,
        };

        const command = new PutObjectCommand(params);
        const response = await s3Client.send(command);

        // 이미지 URL을 생성 (S3의 URL 형식에 맞게)
        const imageUrl = `https://${params.Bucket}.s3.${REGION}.amazonaws.com/${params.Key}`;

        // 에디터에 이미지 삽입
        const editor = editorRef.current.getEditor();
        const range = editor.getSelection();
        editor.insertEmbed(range.index, 'image', imageUrl);
      } catch (error) {
        console.error('Error uploading image: ', error);
      }
    });
  };

  const modules = useMemo(() => {
    return {
      toolbar: {
        container: [
          [{ header: '1' }, { header: '2' }],
          ['bold', 'underline'],
          ['image'],
          [{ list: 'ordered' }, { list: 'bullet' }],
          ['link'],
          [{ align: [] }],
        ],
        handlers: {
          image: imageHandler,
        },
      },
      clipboard: {
        matchVisual: false,
      },
    };
  }, []);

  return (
    <div className="react-quill-container">
      <ReactQuill
        ref={editorRef}
        value={funding.content || ''}
        onChange={handleChange}
        theme="snow"
        modules={modules}
        formats={[
          'header',
          'list',
          'bold',
          'italic',
          'underline',
          'link',
          'image',
          'align',
        ]}
        hooks={{
          addImageBlobHook: handleImageUpload,
        }}
        style={{ height: '350px' }}
      />
    </div>
  );
}

MyEditor.propTypes = {
  funding: propTypes.object.isRequired,
  onChange: propTypes.func.isRequired,
  setRegistActive: propTypes.func.isRequired,
};

export default MyEditor;
